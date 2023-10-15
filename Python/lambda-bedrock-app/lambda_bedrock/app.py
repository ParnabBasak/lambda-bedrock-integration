import os
import json
import boto3
import logging

region = os.environ.get('AWS_DEFAULT_REGION')

# Log Level
logging.basicConfig(level=logging.INFO)

bedrock = boto3.client(service_name='bedrock-runtime',
                       region_name=region)

model_kwargs = {
                "max_tokens_to_sample": 300,
                "temperature": 1,
                "top_p": 0.9 
               }
               
accept = '*/*'
contentType = 'application/json'

def lambda_handler(event, context):
    prompt = "\n\nHuman:explain black holes to 8th graders in 3 sentences\n\nAssistant:"

    model_kwargs["prompt"] = prompt
    body = json.dumps(model_kwargs)

    logging.info(body)
    modelId = 'anthropic.claude-v2'
    response = bedrock.invoke_model(body=body, modelId=modelId, accept=accept,
                                    contentType=contentType)

    response_body = json.loads(response.get('body').read())
    logging.info(response_body)
    logging.info(response_body.get('completion'))
    return_message = response_body.get('completion')

    return {
        "statusCode": 200,
        "body": json.dumps({
            "message": return_message
        }),
    }
