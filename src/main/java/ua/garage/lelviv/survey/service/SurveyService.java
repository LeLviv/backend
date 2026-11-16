package ua.garage.lelviv.survey.service;

import static ua.garage.lelviv.survey.model.SurveyStatus.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.garage.lelviv.survey.dto.request.MakeSurveyRequest;
import ua.garage.lelviv.survey.dto.request.VoteRequest;
import ua.garage.lelviv.survey.dto.response.SurveyOptionDto;
import ua.garage.lelviv.survey.dto.response.SurveyResponseDto;
import ua.garage.lelviv.survey.exception.*;
import ua.garage.lelviv.survey.model.Survey;
import ua.garage.lelviv.survey.model.SurveyOption;
import ua.garage.lelviv.survey.model.SurveyStatus;
import ua.garage.lelviv.survey.model.Vote;
import ua.garage.lelviv.survey.projection.VoteCountProjection;
import ua.garage.lelviv.survey.repository.SurveyRepository;
import ua.garage.lelviv.survey.repository.VoteRepository;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public void vote(Long surveyId, VoteRequest request, Long userId) {
        Survey survey = surveyRepository
                .findById(surveyId)
                .orElseThrow(() -> new SurveyNotFoundException("Таке опитування не було знайдене"));

        if (!(survey.getSurveyStatus() == ACTIVE) || Instant.now().isAfter(survey.getEndTime())) {
            throw new SurveyExpiredException("Опитування закінчилось");
        }

        if (Instant.now().isBefore(survey.getStartTime())) {
            throw new SurveyExpiredException("Опитування ще не почалося");
        }

        if (voteRepository.existsBySurveyIdAndUserId(survey.getId(), userId)) {
            throw new AlreadyVotedException("Ви вже проголосували в цьому опитуванні");
        }

        if (survey.getOptionByPosition(request.getChoice()) == null) {
            throw new OptionNotFoundException("Такого варіанту відповіді немає");
        }

        Vote vote = new Vote();
        vote.setSurveyId(survey.getId());
        vote.setOptionId(request.getChoice());
        vote.setUserId(userId);

        voteRepository.save(vote);
    }

    @Transactional
    public Long makeSurvey(MakeSurveyRequest request) {
        List<SurveyOption> options = IntStream.range(0, request.getOptions().size())
                .mapToObj(i -> {
                    SurveyOption opt = new SurveyOption();
                    opt.setPosition((long) i);
                    opt.setOption(request.getOptions().get(i));
                    return opt;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new InvalidSurveyDataException(
                    "Час закінчення опитування не може бути раніше часу, коли воно починається");
        }

        var survey = Survey.builder()
                .title(request.getTitle())
                .question(request.getQuestion())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .options(options)
                .surveyStatus(ACTIVE)
                .build();

        Survey savedSurvey = surveyRepository.save(survey);
        return savedSurvey.getId();
    }

    @Transactional(readOnly = true)
    public SurveyResponseDto getSurvey(Long id) {
        Survey survey = surveyRepository
                .findById(id)
                .orElseThrow(() -> new SurveyNotFoundException("Такого опитування не існує"));

        List<VoteCountProjection> voteCounts =
                voteRepository.countVotesForSurveysGroupedByOption(List.of(survey.getId()));
        Map<Long, Integer> votesMap = voteCounts.stream()
                .collect(Collectors.toMap(VoteCountProjection::getOptionId, VoteCountProjection::getVoteCount));

        return mapToDto(survey, votesMap);
    }

    public Page<SurveyResponseDto> getSurveys(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("endTime").descending());
        Page<Survey> surveyPage = surveyRepository.findAll(pageable);

        if (surveyPage.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> surveyIds =
                surveyPage.getContent().stream().map(Survey::getId).toList();

        List<VoteCountProjection> voteCounts = voteRepository.countVotesForSurveysGroupedByOption(surveyIds);

        Map<Long, Map<Long, Integer>> votesBySurveyAndOption = voteCounts.stream()
                .collect(Collectors.groupingBy(
                        VoteCountProjection::getSurveyId,
                        Collectors.toMap(VoteCountProjection::getOptionId, VoteCountProjection::getVoteCount)));

        return surveyPage.map(survey -> {
            Map<Long, Integer> optionVotes =
                    votesBySurveyAndOption.getOrDefault(survey.getId(), Collections.emptyMap());
            return mapToDto(survey, optionVotes);
        });
    }

    @Transactional
    public void cancelSurvey(Long id) {
        Survey survey = surveyRepository
                .findById(id)
                .orElseThrow(() -> new SurveyNotFoundException("Опитування з таким Id не знайдено"));

        survey.setSurveyStatus(CANCELED);
    }

    @Transactional
    public void changeSurveyExpiration(Long id, Instant newTime) {
        Survey survey = surveyRepository
                .findById(id)
                .orElseThrow(() -> new SurveyNotFoundException("Опитування з таким id не знайдено"));

        survey.setEndTime(newTime);
    }

    private SurveyResponseDto mapToDto(Survey survey, Map<Long, Integer> votesMap) {
        List<SurveyOptionDto> optionDtos = survey.getOptions().stream()
                .map(opt -> SurveyOptionDto.builder()
                        .position(opt.getPosition())
                        .option(opt.getOption())
                        .votesCount(votesMap.getOrDefault(opt.getPosition(), 0))
                        .build())
                .toList();

        String status = survey.getSurveyStatus().name();
        if (survey.getSurveyStatus() == SurveyStatus.ACTIVE && Instant.now().isAfter(survey.getEndTime())) {
            status = SurveyStatus.EXPIRED.name();
        }

        return SurveyResponseDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .question(survey.getQuestion())
                .startTime(survey.getStartTime())
                .endTime(survey.getEndTime())
                .surveyStatus(status)
                .options(optionDtos)
                .build();
    }
}
