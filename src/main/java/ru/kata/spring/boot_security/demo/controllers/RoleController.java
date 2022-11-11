package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceI;
import ru.kata.spring.boot_security.demo.services.UserServiceI;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class RoleController {
    private final UserServiceI userService;
    private final RoleServiceI roleService;

    public RoleController(UserServiceI userService, RoleServiceI roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

//    @GetMapping("")
//    public String userList(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        model.addAttribute("users", user);
//        model.addAttribute("allUsers", userService.allUsers());
//        return "admin";
//    }
@GetMapping()
public String userList(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    model.addAttribute("users", user);
    model.addAttribute("allUsers", userService.allUsers());
    model.addAttribute("roles", roleService.allRoles());
    return "admin";

}
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
//        model.addAttribute("ID", userService.show(id).getId());
//        model.addAttribute("username", userService.show(id).getUsername());
//        model.addAttribute("surname", userService.show(id).getSurname());
//        model.addAttribute("age", userService.show(id).getAge());
//        model.addAttribute("email", userService.show(id).getEmail());
//        model.addAttribute("roles", userService.show(id).getAuthorities());
        userService.delete(id);
        return "redirect:/admin";
    }

//    @GetMapping("{id}")
//    public String delete(@PathVariable("id") Long id) {
//        userService.delete(id);
//        return "redirect:/admin";
//    }

    @GetMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.show(id));
        model.addAttribute("roles", roleService.allRoles());
        return "admin";
    }


    @GetMapping("/new")
    public String newUSer(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("users", user);
        model.addAttribute("userForm", new User());
        model.addAttribute("roles", roleService.allRoles());
        return "/new";
    }
//    @PostMapping("/edit/{id}")
//    public String update(@RequestParam(value = "roles") Long[] roles, User user) {
//        List<Role> rolesString = roleService.allRoles();
//        List<Role> roles1 = new ArrayList<>(roles.length);
//        for (Long role : roles) {
//            roles1.add(rolesString.get((role.intValue() - 1)));
//        }
//        user.setRoles(new HashSet<>(roleService.saveAll(roles1)));
//        userService.saveUser1(user);
//        return "redirect:/admin";
//    }
@PostMapping("/edit/{id}")
public String update(User user) {

    userService.saveUser1(user);
    return "redirect:/admin";
}


    @PostMapping("/new")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/new";
        }
        if (!userService.newUser(userForm)) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "/new";
        }
        return "redirect:/admin";
    }
}