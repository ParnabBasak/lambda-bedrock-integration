using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using LambdaBedrock.Models;

namespace LambdaBedrock.Builders
{
    internal static class FoundationModelResponseBuilder
    {
        internal static async Task<IFoundationModelResponse?> Build(string modelId, MemoryStream stream)
        {
            IFoundationModelResponse? response = modelId switch
            {
                "anthropic.claude-instant-v1" or "anthropic.claude-v1" or "anthropic.claude-v2" => await JsonSerializer.DeserializeAsync<AnthropicClaudeResponse>(stream, new JsonSerializerOptions() { PropertyNameCaseInsensitive = true }),
                //"stability.stable-diffusion-xl" or "stability.stable-diffusion-xl-v0" => await JsonSerializer.DeserializeAsync<StableDiffusionXLResponse>(stream, new JsonSerializerOptions() { PropertyNameCaseInsensitive = true }),
                _ => throw new NotSupportedException($"ModelId {modelId} not supported"),
            };
            return response;
        }
    }
}