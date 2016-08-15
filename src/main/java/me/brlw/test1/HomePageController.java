package me.brlw.test1;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by vl on 13.08.16.
 */
@RequestMapping("/")
@Controller
public class HomePageController
{
    @RequestMapping(method = RequestMethod.GET)
    public String homePage(Model uiModel) {
        return "homepage";
    }
}
