package lambdabedrock;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import util.BedrockRequestBody;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Map<String,String>, APIGatewayProxyResponseEvent> {

    Region region = Region.of(System.getenv("AWS_REGION"));

    BedrockRuntimeClient runtime = BedrockRuntimeClient.builder()
                .region(region)
                .build();

    private static final String PROMPT = """
                Generate a list of names for a fun brand of chili sauce    
                    """;

    // Uncomment the model Id you want to use. Only one model Id is allaowed.                    
    private static final String MODEL_ID = "anthropic.claude-v2";
    //private static final String MODEL_ID = "ai21.j2-mid-v1";
    //private static final String MODEL_ID = "amazon.titan-tg1-large";
    //private static final String MODEL_ID = "cohere.command-text-v14";

    @Override
    public APIGatewayProxyResponseEvent handleRequest(Map<String,String> event, Context context) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String bedrockBody = BedrockRequestBody.builder()
                .withModelId(MODEL_ID)
                .withPrompt(PROMPT)
                .withInferenceParameter("max_tokens_to_sample", 2048)
                .withInferenceParameter("temperature", 0.5)
                .withInferenceParameter("top_k", 250)
                .withInferenceParameter("top_p", 1)
                .build();

        InvokeModelRequest invokeModelRequest = InvokeModelRequest.builder()
                .modelId(MODEL_ID)
                .body(SdkBytes.fromString(bedrockBody, Charset.defaultCharset()))
                .build();
        
        try {
            
            InvokeModelResponse model_response = runtime.invokeModel(invokeModelRequest);

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
