package xml;
import java.io.IOException;
import javax.swing.*;

public final class gui_tree extends JFrame
{
    public gui_tree() throws IOException
    {
            draw_Frame(); 
            treePanel gui= new treePanel();
            add(gui);
    }
    
    // draw panel 
        public void draw_Frame()
        {

           
            setVisible(true);
            setSize(1800,1000);
            //setLocation(JFrame);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
        }
}