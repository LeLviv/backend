package ua.garage.lelviv.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.garage.lelviv.survey.model.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {}
