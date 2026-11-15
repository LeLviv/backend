package ua.garage.lelviv.survey.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.garage.lelviv.survey.model.Vote;
import ua.garage.lelviv.survey.projection.VoteCountProjection;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsBySurveyIdAndUserId(Long surveyId, Long userId);

    Integer countBySurveyIdAndOptionId(Long surveyId, Long optionId);

    @Query("SELECT v.surveyId AS surveyId, v.optionId AS optionId, COUNT(v.id) AS voteCount "
            + "FROM Vote v WHERE v.surveyId IN :surveyIds GROUP BY v.surveyId, v.optionId")
    List<VoteCountProjection> countVotesForSurveysGroupedByOption(@Param("surveyIds") List<Long> surveyIds);
}
