package greet;

public class Morning extends Greetable{
    @Override
    public String buildResponse(String usernName) {
        return "Good Morning " + usernName;
    }
}
