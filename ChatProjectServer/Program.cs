using System;
using System.Diagnostics;

namespace ChatProjectServer
{
    class Program
    {
        static void Main(string[] args)
        {
            using Process process = new Process();
            process.StartInfo.WorkingDirectory = "Java";
            process.StartInfo.FileName = "java";
            process.StartInfo.Arguments = "--enable-preview -jar ChatProject.jar";
            process.OutputDataReceived += (sender, e) => Console.WriteLine(e.Data);
            process.ErrorDataReceived += (sender, e) => Console.WriteLine(e.Data);
            process.StartInfo.RedirectStandardOutput = true;
            process.StartInfo.RedirectStandardError = true;
            process.Start();
            process.BeginOutputReadLine();
            process.WaitForExit();
        }
    }
}
