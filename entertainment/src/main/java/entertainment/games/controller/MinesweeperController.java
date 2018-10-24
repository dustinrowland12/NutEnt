package entertainment.games.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/games")
public class MinesweeperController {
	
	@GetMapping("/minesweeper")
	public String minesweeper(Model model) {
		System.out.println("Minesweeper page output");
		return "games/minesweeper";
	}

}
