import { BedrockRuntime } from '@aws-sdk/client-bedrock-runtime';

const region_code = process.env.AWS_REGION;
const bedrockRuntime = new BedrockRuntime({
    region: region_code,
});

const prompt = 'Explain to me about AWS Lambda in 3 lines.';
const claudePrompt = `\n\nHuman: ${prompt} \n\nAssistant:`;

const config = {
    prompt: claudePrompt,
    max_tokens_to_sample: 2048,
    temperature: 0.5,
    top_k: 250,
    top_p: 1,
    stop_sequences: ['\n\nHuman:'],
};

export const lambdaHandler = async (event, context) => {
    var value = '';
    try {
        const response = await bedrockRuntime.invokeModel({
            body: JSON.stringify(config),
            modelId: 'anthropic.claude-v2',
            accept: 'application/json',
            contentType: 'application/json',
        });

        value = JSON.parse(response.body.transformToString());

        console.log(value.completion);
        console.log(`\nStop reason: ${value.stop_reason}`);
    } catch (err) {
        console.log(err);
    }

    try {
        return {
            statusCode: 200,
            body: JSON.stringify({
                message: value.completion,
            }),
        };
    } catch (err) {
        console.log(err);
    }
};
