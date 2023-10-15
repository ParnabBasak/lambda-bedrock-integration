namespace LambdaBedrock.Models;

public class AnthropicClaudeRequest
{
    public AnthropicClaudeRequest(string prompt)
    {
        this.prompt = prompt;
    }
    
    public string prompt { get; set; }
    public int max_tokens_to_sample { get; set; } = 400;
    public double temperature { get; set; } = 0.7;
    public double top_p { get; set; } = 1;
    public int top_k { get; set; } = 250;
    public object[] stop_sequences { get; set; } = new object[0];
}

