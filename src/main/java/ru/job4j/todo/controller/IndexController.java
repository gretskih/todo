package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/done/{id}")
    public String setTaskDone(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено.");
            return "errors/404";
        }
        Task task = taskOptional.get();
        task.setDone(true);
        taskService.replace(id, task);
        return "redirect:/index";
    }

    @GetMapping("/completed")
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

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено.");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "view";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено.");
            return "errors/404";
        }
        return "redirect:/index";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
                model.addAttribute("message", "Задание с указанным идентификатором не найдено.");
                return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "edit";
    }

    @PostMapping("/update")
    public String update(Task task, Model model) {
        boolean isReplaced = taskService.replace(task.getId(), task);
        if (!isReplaced) {
            model.addAttribute("message", "Обновление завершилось ошибкой.");
            return "errors/404";
        }
        return "redirect:/index";
    }

}
