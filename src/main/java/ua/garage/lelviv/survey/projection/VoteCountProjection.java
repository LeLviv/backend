package ua.garage.lelviv.survey.projection;

public interface VoteCountProjection {
    Long getSurveyId();

    Long getOptionId();

    Long getVoteCount();
}
