package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;
    private TacoRepository designRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo,TacoRepository designRepo) {
        this.ingredientRepo = ingredientRepo;
        this.designRepo = designRepo;
    }


    @GetMapping
    public String showDesignForm(Model model){
//        List<Ingredient> ingredients = Arrays.asList(
//          new Ingredient("FLTO","Flour Tortilla", Type.WARP),
//          new Ingredient("COTO","Corn Tortilla",Type.WARP),
//          new Ingredient("GRBF","Ground Beef",Type.PROTEIN),
//          new Ingredient("CARN","Carnitas",Type.PROTEIN),
//          new Ingredient("TMTO","Diced Tomatoes",Type.VEGGIES),
//          new Ingredient("LETC","Lettuce",Type.VEGGIES),
//          new Ingredient("CHED","cheddar",Type.CHEESE),
//          new Ingredient("JACK","Monterrey Jack",Type.CHEESE),
//          new Ingredient("SLSA","Salsa",Type.SAUCE),
//          new Ingredient("SRCR","Sour Cream",Type.SAUCE)
//        );
//        Type[] tpyes = Ingredient.Type.values();
//        for (Type type : tpyes) {
//            model.addAttribute(type.toString().toLowerCase(),
//                    filterByType(ingredients,type));
//        }
//        model.addAttribute("design",new Taco());
//
//        return "design";

        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

        return "design";
    }

    @ModelAttribute(name = "order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco(){
        return new Taco();
    }

    private Object filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream().filter(x->x.getType().equals(type)).collect(Collectors.toList());
    }


    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors,@ModelAttribute Order order){
        if(errors.hasErrors()){
            return "design";
        }
        Taco saved = designRepo.save(design);
        order.addDesign(saved);
        log.info("processing design:"+design);
        return "redirect:/orders/current";
    }
}
