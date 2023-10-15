# Serverless Bedrock Integration
This sample repository has sample code to integrate AWS Lambda with Amazon Bedrock.

AWS Services used:
1. [AWS Lambda](https://aws.amazon.com/lambda/)
2. [AWS SAM](https://aws.amazon.com/serverless/sam/)

The following Lambda Language Runtimes are supported:

| Language | Runtime Version    | AWS SDK Version |
| :---:   | :---: | :---: |
| .NET  | v6.0   | 3.7.201.5 |
| Java | v17 | 2.20.157 |
| NodeJS | v18 | 3.428.0 |
| Python | v3.11 | 1.28.57 |

> **Warning**
> This application is not ready for production use. It was written for demonstration and educational purposes. Review the [Security](#security) section of this README and consult with your security team before deploying this stack. No warranty is implied in this example.

## Deployment instructions

### Prerequisites

- [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html)
- Language specific runtimes for local testing

### Cloning the repository

Clone this repository:

```bash
git clone git@github.com:ParnabBasak/lambda-bedrock-integration.git
```

### Amazon Bedrock setup

This application can be used with a variety of LLMs via Amazon Bedrock. See [Supported models in Amazon Bedrock](https://docs.aws.amazon.com/bedrock/latest/userguide/what-is-service.html#models-supported) for a complete list.

By default, this application uses **Anthropic Claude v2** to generate responses.

> **Important**
> Before you can use these models with this application, **you must request access in the Amazon Bedrock console**. See the [Model access](https://docs.aws.amazon.com/bedrock/latest/userguide/model-access.html) section of the Bedrock User Guide for detailed instructions.
> By default, this application is configured to use Amazon Bedrock in the supported Region. Make sure you request model access in that Region (this does not have to be the same Region that you deploy this stack to).

## License

This library is licensed under the MIT-0 License. See the [LICENSE](LICENSE) file.