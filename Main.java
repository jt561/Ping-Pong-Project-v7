/**
 * A template main-method class for use from
 * non-BlueJ IDEs or the command line.
 * 
 * @author kg246
 * @version 2015.12.22
 */
public class Main
{
    private Main() {}
    
    public static void main(String[] args)
    {
        Program prog = new Program();
        prog.shell();
        prog.finish();
        System.exit(0);
    }
}
