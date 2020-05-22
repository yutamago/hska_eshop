package de.hska.eshopapi.core.product.controllers;

import de.hska.eshopapi.core.product.dao.ProductDAO;
import de.hska.eshopapi.core.product.model.Product;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;
import java.util.UUID;

@RestController
@RequestMapping(path = "/dev", name = "Dev", produces = {"application/json"})
@Api(tags = "Dev")
public class DevController {
    private final ProductDAO productDAO;

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public DevController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> setup() {
        Product spielzeug = new Product();
        spielzeug.setProductId(UUID.fromString("1edca507-00a3-4d46-b87e-4dbcfa6096b5"));
        spielzeug.setCategoryId(UUID.fromString("0ccf12d2-f2d2-4d51-816b-6e2624e5b664"));
        spielzeug.setName("LEGO Drachenburg");
        spielzeug.setDetails("Ein Spaß für die ganze Familie");
        spielzeug.setPrice(new BigDecimal("14.99"));

        Product buch = new Product();
        buch.setProductId(UUID.fromString("fc927e8a-95d3-447f-bac6-759ac273e1e6"));
        buch.setCategoryId(UUID.fromString("0c708787-0cff-4444-8b55-d7179388db10"));
        buch.setName("Die Tribute von Panem X: Das Lied von Vogel und Schlange");
        buch.setDetails("Im Kapitol macht sich der 18-jährige Coriolanus Snow bereit, als Mentor bei den Hungerspielen zu Ruhm und Ehre zu gelangen.");
        buch.setPrice(new BigDecimal("26.00"));

        this.productDAO.save(spielzeug);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
