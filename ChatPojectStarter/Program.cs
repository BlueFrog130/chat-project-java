using System.Diagnostics;

namespace ChatPojectClient
{
    class Program
    {
        static void Main(string[] args)
        {
            using Process process = new Process();
            process.StartInfo.WorkingDirectory = "Java";
            process.StartInfo.FileName = "java";
            process.StartInfo.Arguments = "--enable-preview --module-path lib --add-modules javafx.controls,javafx.fxml -jar ChatProjectClient.jar";
            process.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;
            process.StartInfo.CreateNoWindow = true;
            process.Start();
            process.WaitForExit();
        }
    }
}
