package com.example.foodtocarbon;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "";
    }

    @GetMapping("/foodlist")
    public float foodlist(@RequestParam List<String> food, @RequestParam List<String> q)

    {
        float food_emision = 0;

        try {

            System.out.println(food);
            System.out.println(q);
            String s="";
            for (String f: food) {
                System.out.println(f);
                s += "food=" + f + "&";
            }
            System.out.println(s);

            Map<String, String> map1 = IntStream.range(0, food.size())
                    .boxed()
                    .collect(Collectors.toMap(food::get, q::get));

            System.out.println(map1);

            final String uri = "http://127.0.0.1:8080/v2/carbon-emissions/?" + s;

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);

            System.out.println(result);

            JsonParser springParser = JsonParserFactory.getJsonParser();
            List < Object > list = springParser.parseList(result);
            for (Object o: list) {
                if (o instanceof Map) {
                    Map < String, String > map = (Map < String, String > ) o;
                    System.out.println(map.get("food"));
                    System.out.println(map.get("value"));
                    System.out.println(map1.get(map.get("food")));
                    food_emision += Float.parseFloat(map.get("value")) * Float.parseFloat(map1.get(map.get("food")));
                    int i = 0;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return food_emision;
    }

}
