package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

@ThreadSafe
@Controller
@AllArgsConstructor
@RequestMapping({"/tasks"})
public class TaskController {

    private TaskService taskService;
    private PriorityService priorityService;

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "index";
    }

    @GetMapping("/done/{id}")
    public String setTaskDone(Model model, @PathVariable int id) {
        boolean isReplaced = taskService.setDoneTrue(id);
        if (!isReplaced) {
            model.addAttribute("message", "Обновление завершилось ошибкой.");
            return "errors/404";
        }
        return "redirect:/tasks";
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
    public String getCreationPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        return "create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, @SessionAttribute User user, Model model) {
        task.setUser(user);
        var taskOptional = taskService.add(task);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Произошла ошибка. Задание не добавлено.");
            return "errors/404";
        }
        return "redirect:/tasks";
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
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        model.addAttribute("priorities", priorityService.findAll());
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
        return "redirect:/tasks";
    }

}
