package controllers;

import play.mvc.*;

public class HomeController extends Controller {
    public Result index(Http.Request request) {
        return ok(views.html.home.render("Beranda Klinik", request));
    }
}
