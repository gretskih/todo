package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

@ThreadSafe
@Controller
@AllArgsConstructor
@RequestMapping({"/", "/index"})
public class IndexController {

    private TaskService taskService;

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "index";
    }

    @GetMapping("/done")
    public String getIndexDone(Model model) {
        model.addAttribute("tasks", taskService.findAllDone());
        return "index";
    }

    @GetMapping("/new")
    public String getIndexNew(Model model) {
        model.addAttribute("tasks", taskService.findAllNew());
        return "index";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Model model) {
        try {
            taskService.add(task);
            return "redirect:/index";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }
}
