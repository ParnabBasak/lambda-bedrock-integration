package lambdabedrock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Map<String,String>, APIGatewayProxyResponseEvent> {

    //Region region = Region.US_EAST_1;
    Region region = Region.of(System.getenv("AWS_REGION"));

    BedrockRuntimeClient runtime = BedrockRuntimeClient.builder()
                .region(region)
                .build();

    String prompt = "Generate a list of names for a fun brand of chili sauce";

    @Override
    public APIGatewayProxyResponseEvent handleRequest(Map<String,String> event, Context context) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        JSONObject jsonBody = new JSONObject()
                .put("prompt", "Human: " + prompt + " Assistant:")
                .put("temperature", 0.8)
                .put("max_tokens_to_sample", 1024);

        SdkBytes body = SdkBytes.fromUtf8String(
               jsonBody.toString()
        );

        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId("anthropic.claude-v2")
                .body(body)
                .build();
        
        try {
            
            InvokeModelResponse model_response = runtime.invokeModel(request);

            JSONObject jsonObject = new JSONObject(
                    model_response.body().asString(StandardCharsets.UTF_8)
            );

            String completion = jsonObject.getString("completion");

            return response
                    .withStatusCode(200)
                    .withBody(completion);
        } catch (Exception e) {
            return response
                    .withBody("{}")
                    .withStatusCode(500);
        }
    }
}
