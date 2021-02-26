package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {

    @GetMapping
    public String showDesignForm(Model model){
        List<Ingredient> ingredients = Arrays.asList(
          new Ingredient("FLTO","Flour Tortilla", Type.WARP),
          new Ingredient("COTO","Corn Tortilla",Type.WARP),
          new Ingredient("GRBF","Ground Beef",Type.PROTEIN),
          new Ingredient("CARN","Carnitas",Type.PROTEIN),
          new Ingredient("TMTO","Diced Tomatoes",Type.VEGGIES),
          new Ingredient("LETC","Lettuce",Type.VEGGIES),
          new Ingredient("CHED","cheddar",Type.CHEESE),
          new Ingredient("JACK","Monterrey Jack",Type.CHEESE),
          new Ingredient("SLSA","Salsa",Type.SAUCE),
          new Ingredient("SRCR","Sour Cream",Type.SAUCE)
        );
        Type[] tpyes = Ingredient.Type.values();
        for (Type type : tpyes) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients,type));
        }
        model.addAttribute("design",new Taco());

        return "design";
    }

    private Object filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream().filter(x->x.getType().equals(type)).collect(Collectors.toList());
    }


    @PostMapping
    public String processDesign(@Valid @ModelAttribute("design") Taco design, Errors errors){
        if(errors.hasErrors()){

            return "design";
        }
        log.info("processing design:"+design);
        return "redirect:/orders/current";
    }
}
