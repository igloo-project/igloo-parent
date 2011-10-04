package fr.openwide.springmvc.web.controller.aide;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("aideController")
@RequestMapping("aide")
public class AideController {

	private static final String DEFAULT_VIEW = "aide";

	@RequestMapping
	public String render(Model model) {
		return DEFAULT_VIEW;
	}
}
