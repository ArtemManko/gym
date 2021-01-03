package by.pvt.spring.webproject.controllers;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.repository.ScheduleRepository;
import by.pvt.spring.webproject.service.ScheduleService;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class ScheduleController {

    @Autowired
    UserService userService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ScheduleService scheduleService;

    //CREATE new Schedule
    @GetMapping("/schedule-create")
    public String createScheduleGet(ScheduleWorkout scheduleWorkout, Model model) {
        scheduleService.attributes(scheduleWorkout, model);
        return "block/schedule/scheduleCreate";
    }

    @PostMapping("/schedule-create")
    public String createSchedulePost(
            @Valid ScheduleWorkout scheduleWorkout,
            @RequestParam(required = false) Long id,
            Model model) {
        System.out.println(scheduleWorkout.getDays());
        scheduleService.timeWorkouts();

        //Check values, not be null
        if (!scheduleService.createScheduleNull(scheduleWorkout) || id == null) {
            scheduleService.attributes(scheduleWorkout, model);
            model.addAttribute("errorValue", "Set the value!");
            return "block/schedule/scheduleCreate";
        }
//Check new Schedule, if exist
        if (!scheduleService.checkScheduleExist(scheduleWorkout)) {
            scheduleService.attributes(scheduleWorkout, model);
            model.addAttribute("errorSchedule", "Schedule exist!");
            return "block/schedule/scheduleCreate";
        }
        scheduleService.addSchedule(id, scheduleWorkout);
        return "redirect:/schedule-list";
    }

    //Schedule delete and send email for users
    @GetMapping("schedule-delete/{id}")
    public String deleteSchedule(@PathVariable("id") Long id) {
        scheduleService.deleteSchedule(id);
        return "redirect:/schedule-list";
    }

    //List schedule for Admin
    @GetMapping("/schedule-list")
    public String scheduleList(Model model) {
        model.addAttribute("schedules", scheduleRepository.findAll());
        return "block/schedule/scheduleList";
    }

    //List schedule for client, by level
    @GetMapping("/{level}/{id}")
    public String scheduleGet(
            @PathVariable("level") Level level,
            User user,
            Model model) {

        model.addAttribute("user", user);
        model.addAttribute("schedules", scheduleService.level(level));

        return "block/schedule/schedule";
    }

    //Sung up workouts by level
    @GetMapping("schedule-singup/{level}/{id}/{id_schedule}")
    public String singUpSchedule(
            @PathVariable("level") Level level,
            @PathVariable("id") Long id,
            @PathVariable("id_schedule") Long id_schedule
    ) {

        ScheduleWorkout scheduleWorkout = scheduleService.findById(id_schedule);
        User user = userService.findById(id);
//Check if you have this schedule
        if (!scheduleService.checkSingUpClient(id, id_schedule)) {
            return "redirect:/{level}/{id}";
        }
        scheduleService.singUpClient(scheduleWorkout, user);
        return "redirect:/{level}/{id}";

    }

    //Schedule EDIT
    @GetMapping("/schedule-edit/{id}")
    public String editScheduleForm(
            @PathVariable("id") Long id,
            Model model) {
        ScheduleWorkout scheduleWorkout = scheduleService.findById(id);
        scheduleService.attributes(scheduleWorkout, model);
        return "block/schedule/scheduleEdit";
    }

    @PostMapping("/schedule-edit/{id}")
    public String editUser(ScheduleWorkout scheduleWorkout, Model model) {
        if (!scheduleService.createScheduleNull(scheduleWorkout)) {
            scheduleService.attributes(scheduleWorkout, model);
            model.addAttribute("errorValue", "Set the value!");
            return "block/schedule/scheduleEdit";
        }
        if (!scheduleService.editSchedule(scheduleWorkout)) {
            scheduleService.attributes(scheduleWorkout, model);
            model.addAttribute("errorSchedule", "Schedule exist!");
            return "block/schedule/scheduleEdit";
        }
        scheduleService.save(scheduleWorkout);
        return "redirect:/schedule-list";
    }
}