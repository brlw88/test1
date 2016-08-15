package me.brlw.test1;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by vl on 07.08.16.
 */

@RequestMapping("/users")
@Controller
public class UserController
{
    private static final int MIN_SEARCH_STR_LENGTH = 3;

    private static final Logger LOG = Logger.getLogger(UserController.class);

    private UserService userService;
    private MessageSource messageSource;

    private void createPagerAttributes(Model uiModel, Page<User> page)
    {
        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 2);
        int end = Math.min(begin + 4, page.getTotalPages());

        uiModel.addAttribute("beginIndex", begin);
        uiModel.addAttribute("endIndex", end);
        uiModel.addAttribute("currentIndex", current);
        uiModel.addAttribute("totalPages", page.getTotalPages());
    }

    private boolean searchStrValid(String search_str)
    {
        if (null != search_str)
            return (search_str.trim().length() >= MIN_SEARCH_STR_LENGTH);
        else
            return false;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value="page", required = false) Integer pageNum, Model uiModel, Locale locale)
    {
        LOG.info("Listing users");
        Page<User> page = userService.findAll(pageNum == null ? 1 : pageNum);

        uiModel.addAttribute("users", page.getContent());
        uiModel.addAttribute("search_str", null);

        createPagerAttributes(uiModel, page);

        if (page.getTotalElements() == 0)
            uiModel.addAttribute("message", new Message("warning",
                    messageSource.getMessage("user_list_empty", new Object[]{}, locale)));

        LOG.info("Listed users: " + page.getNumberOfElements());
        return "users/list";
    }

    @RequestMapping(params = "search", method = RequestMethod.GET)
    public String search(@RequestParam("search") String search_str,
                         @RequestParam(value="page", required = false) Integer pageNum,
                         Model uiModel,
                         Locale locale)
    {
        if (!searchStrValid(search_str)) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("search_string_not_valid", new Object[]{}, locale)));
            return list(pageNum, uiModel, locale);
        }
        else
        {
            LOG.info("Searching users by " + search_str);
            Page<User> page = userService.findAllByName(pageNum == null ? 1 : pageNum, search_str);

            uiModel.addAttribute("users", page.getContent());
            uiModel.addAttribute("search_str", search_str);

            createPagerAttributes(uiModel, page);

            if (page.getTotalElements() == 0)
                uiModel.addAttribute("message", new Message("warning",
                        messageSource.getMessage("user_search_empty", new Object[]{}, locale)));

            LOG.info("Found users: " + page.getNumberOfElements());
            return "users/list";
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String doSearch(@RequestParam("search_str") String search_str, Model uiModel, HttpServletRequest httpServletRequest, Locale locale)
    {
        if (!searchStrValid(search_str)) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("search_string_not_valid", new Object[]{}, locale)));
            return list(null, uiModel, locale);
        }
        else
            return "redirect:/users?search=" + UrlUtil.encodeUrlPathSegment(search_str, httpServletRequest);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel)
    {
        User user = userService.findById(id);
        uiModel.addAttribute("user", user);
        return "users/show";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        SimpleDateFormat dateFmt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        binder.registerCustomEditor(Date.class, "createdDate", new CustomDateEditor(dateFmt, false));
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String update(@Valid User user, BindingResult bindingResult, Model uiModel,
                         HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
                         Locale locale) {
        LOG.info("Updating user: " + user.toString());
        if (bindingResult.hasErrors()) {
            for(ObjectError e: bindingResult.getAllErrors())
                LOG.warn(e.toString());
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("user_save_fail", new Object[]{}, locale)));
            uiModel.addAttribute("user", user);
            return "users/update";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("user_save_success", new Object[]{}, locale)));
        user = userService.save(user);
        return "redirect:/users/" + UrlUtil.encodeUrlPathSegment(user.getId().toString(),
                httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("user", userService.findById(id));
        return "users/update";
    }

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        User user = new User();
        uiModel.addAttribute("user", user);
        return "users/update";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Long id, Model uiModel, Locale locale)
    {
        LOG.info("Deleting user");
        User user = userService.findById(id);
        if (user != null) {
            userService.delete(user);
            uiModel.addAttribute("message", new Message("success",
                    messageSource.getMessage("user_delete_success", new Object[]{}, locale)));
        }
        else
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("user_delete_error", new Object[]{}, locale)));
        return list(null, uiModel, locale);
    }


    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}



