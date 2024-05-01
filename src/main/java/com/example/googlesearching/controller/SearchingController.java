package com.example.googlesearching.controller;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchingController {

    @GetMapping("/")
    public String getForm() {
        return "index5";
    }

    @GetMapping("/search")
    public String search(@RequestParam String q, Model model) throws IOException {

        String url = "https://www.google.com/search?q=" + q;
        Document document = Jsoup.connect(url).get();
        Elements searchResults = document.select("div.g");
        List<String> results = new ArrayList<>();
        model.addAttribute("results", results);
        for (Element result : searchResults) {
            Element title = result.select("h3").first();
            Element link = result.select("a[href]").first();
            if (title != null && link != null) {
                results.add(title.text() + " - " + link.absUrl("href"));
            }
        }

        writeToFile(results);
        return "index5";
    }

    private void writeToFile(List<String> results) throws IOException {
        FileWriter writer = new FileWriter("search_results.txt");
        for (String result : results) {
            writer.write(result + "\n");
        }
        writer.close();
    }
}
