package nutent.entertainment.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("games")
public class MazeController {
       
	@GetMapping("/maze")
	public String maze(Model model) {
		System.out.println("Maze page output");
		return "games/maze";
	}

}
