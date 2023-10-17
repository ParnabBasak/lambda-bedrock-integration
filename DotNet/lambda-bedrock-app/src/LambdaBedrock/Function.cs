using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Http;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.IO;

using Amazon;
using Amazon.Lambda.Core;
using Amazon.Lambda.APIGatewayEvents;

using Amazon.BedrockRuntime;
using Amazon.BedrockRuntime.Model;

using LambdaBedrock.Builders;
using LambdaBedrock.Models;

// Assembly attribute to enable the Lambda function's JSON input to be converted into a .NET class.
[assembly: LambdaSerializer(typeof(Amazon.Lambda.Serialization.SystemTextJson.DefaultLambdaJsonSerializer))]

namespace LambdaBedrock
{
    public class Function
    {
        private readonly AmazonBedrockRuntimeClient _bedrockRuntimeClient;

        public Function()
        {
            RegionEndpoint? region = RegionEndpoint.GetBySystemName(Environment.GetEnvironmentVariable("AWS_REGION"));
            this._bedrockRuntimeClient = new AmazonBedrockRuntimeClient(region);
        }

        private static async Task<IFoundationModelResponse?> InvokeBedrock(string prompt,AmazonBedrockRuntimeClient _bedrockRuntimeClient)
        {
            byte[] byteArray = Encoding.UTF8.GetBytes(JsonSerializer.Serialize(new AnthropicClaudeRequest(prompt)));
            MemoryStream stream = new MemoryStream(byteArray);

            string modelId = "anthropic.claude-v2";
            var result = await _bedrockRuntimeClient.InvokeModelAsync(new InvokeModelRequest()
            {
                ModelId = modelId,
                Body = stream,
                ContentType = "application/json",
                Accept = "application/json"
            });

            var response = await FoundationModelResponseBuilder.Build(modelId, result.Body);

            return response;
        }

        public async Task<APIGatewayProxyResponse> FunctionHandler(APIGatewayProxyRequest apigProxyEvent, ILambdaContext context)
        {

            string prompt = "\n\nHuman:explain black holes to 8th graders in 3 sentences\n\nAssistant:";
            
            var model_response = await InvokeBedrock(prompt, _bedrockRuntimeClient);
            var body = model_response.GetResponse();

            return new APIGatewayProxyResponse
            {
                Body = JsonSerializer.Serialize(body),
                StatusCode = 200,
                Headers = new Dictionary<string, string> { { "Content-Type", "application/json" } }
            };
        }
    }
}
