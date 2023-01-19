package greet;

public class Evening extends Greetable{
    @Override
    public String buildResponse(String usernName) {
        return "Good evening " + usernName;
    }
}
