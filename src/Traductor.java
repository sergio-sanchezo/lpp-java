public class Traductor extends GramaticaBaseListener {

    @Override
    public void enterS(GramaticaParser.SContext ctx) {
        System.out.println("enterS");
    }

}
