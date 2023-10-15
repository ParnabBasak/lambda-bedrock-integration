using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LambdaBedrock.Models
{
    public interface IFoundationModelResponse
    {
        public string? GetResponse();

        public string? GetStopReason();
    }
}