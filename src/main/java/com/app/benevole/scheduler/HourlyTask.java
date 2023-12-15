package com.app.benevole.scheduler;

import com.app.benevole.model.Horaire;
import com.app.benevole.repository.HoraireRepository;
import com.app.benevole.service.HoraireService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class HourlyTask {

    private final HoraireService horaireService;


    private final HoraireRepository hr;

    private final LocalDateTime now = LocalDateTime.now();

    public HourlyTask(HoraireService horaireService, HoraireRepository hr) {
        this.horaireService = horaireService;
        this.hr = hr;
    }

    /**
     * Méthode de vérification de la validité des horaires disponibles en BD
     */
    @Scheduled(fixedRate = 60000)
    public void disableHourlies() {
        List<Horaire> h = horaireService.all(true);
        for (Horaire horaire : h) {
            if (now.isAfter(horaire.getEndDate())) {
                horaire.setState(false);
                hr.saveAndFlush(horaire);
            }
        }
    }

}
