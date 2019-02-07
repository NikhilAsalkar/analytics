package util;
import bean.Representation;

public class CreateDocument {
    Representation representation = new Representation();
    String message;

    public String MessageJson(Representation respresentation)
    {


        String json = "{"+
                "\"success\":\""+respresentation.isSuccess()+"\","+
                "\"error\":\""+respresentation.getError()+"\","+
                "\"payload\":"+respresentation.getPayload()+
                "}";


        return json;
    }

    public String SendMessage(boolean success,String error,Object payload)
    {
        representation.setSuccess(success);
        representation.setError(error);
        representation.setPayload(payload);
        message = new CreateDocument().MessageJson(representation);
        return message;
    }

}
