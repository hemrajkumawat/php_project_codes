package xml;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class node_position
{
    public int node_x=0;
    public int node_y=0;
    public node_position()
    {
        node_x=0;
        node_y=0;
    }
}
public class treePanel extends JPanel implements MouseListener, ActionListener
{
    public static Dimension panel_area_size= new Dimension(1800,1000);
    public static int line_x1=0, line_x2=0, line_y1=0, line_y2=0,parent_node=0,i=0,j=0,node_number;
    public static int unit_width=0,h=0,w=0,x,y;
    public static int unit_height=0,node_num=0;
    public static int k=0;
    public static int grid[][]= new int[2*xmlparser.max_depth+6][2*xmlparser.maxm_diameter+6];  //used for searching the node and query
    public static int cal_position_in_level[] = new int[xmlparser.max_depth+2];  
    public static node_position node_location[] = new node_position[xmlparser.node_number+5];
    public treePanel()throws IOException
    {
        for(int node=0;node<=xmlparser.node_number+2;node++)
        {
            node_location[node]= new node_position();
        }
        //creating the paint area panel
        make_grid(); //stores which node is at grid[i][j]
        find_cell_number(); 
   
        addMouseListener(this);
        setPreferredSize(panel_area_size);
      //  System.out.println("Tree panel class called");
        setBorder(BorderFactory.createLineBorder(Color.black, 2));
       // System.out.println("Maxm depth ="+xmlparser.maxm_diameter+" and maxm diameter= "+xmlparser.max_depth);
       
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
         g.setColor(Color.WHITE);
         g.fillRect(0, 0, this.getWidth(), this.getHeight());
                         
         Graphics2D g2 = (Graphics2D)g;
             w= this.getWidth();
             h = this.getHeight();
              

            
            unit_height= h/(2*(xmlparser.max_depth+1));
            unit_width= w/(2*xmlparser.maxm_diameter);
            
           
             addNode(g2); // all it for creating the nodes and edges
    } // paint component ended
    
    
    public void addNode(Graphics2D g2d)
    {
     //   System.out.println("Unit height"+ unit_height+ " and unit width= " + unit_width);
        for(int i=0;i<=2*xmlparser.max_depth+2;i++)
        {
            for(int j=0;j<=2*xmlparser.maxm_diameter+2;j++)
            {
                if(grid[i][j]!=-1)
                {
                    
              //      System.out.println("Node = "+grid[i][j]+" i ="+i +" and j= "+j);
                    node_num= grid[i][j];
                    y= node_location[node_num].node_y;
                    x= node_location[node_num].node_x; 
                  //  System.out.println("x "+x+" y= "+y);
                    g2d.setColor(Color.BLACK);
                    if(node_num==0)
                    {
                         g2d.setColor(Color.RED);
                        g2d.fillOval(x*unit_width,y*unit_height,unit_width , unit_height);
                       
                    }
                    else {
                       // g2d.setColor(Color.BLACK);
                    	g2d.fillOval(x*unit_width,y*unit_height,unit_width , unit_height);
                        g2d.setColor(Color.black);
                    }
                    
                    g2d.drawString(xmlparser.tag_obj[node_num].tag_name,x*unit_width+2,y*unit_height);
                    parent_node= xmlparser.tag_obj[node_num].parent_name;
                    line_x1=node_location[parent_node].node_x;
                    line_y1= node_location[parent_node].node_y;
                    line_x2= x;
                    line_y2=y;
                    g2d.drawLine(unit_width*line_x1+15, unit_height*line_y1, unit_width*line_x2+15, unit_height*line_y2);
                }
            }
        }
    }// end of addNode
    
    
    // find the location of each node 
    public void find_cell_number()
    {
        for(int idx=0;idx<=xmlparser.max_depth;idx++)
        {
            cal_position_in_level[idx]= xmlparser.maxm_diameter/xmlparser.level_diameter[idx];
        }
        int x=0,y=0;
        for(int idx=0;idx<=xmlparser.max_depth;idx++)// run for each loop
        {
            k=1;
            x= (2*idx);
           
            for(int node=0;node<=xmlparser.node_number;node++)
            {
                if(xmlparser.tag_obj[node].level_number==idx)
                {
                    y=k*cal_position_in_level[idx]; k=k+2;
                    grid[x][y]=node; // node is situtated at cell (x,y)
                    node_location[node].node_x=y;
                    node_location[node].node_y=x;
                }
            }
        }
    }
    
    
    // divide the whole window in grids and initialize it by -1//
    public void make_grid()
    {
        for(int i=0;i<=(2*xmlparser.max_depth+4);i++)
        {
            for(int j=0;j<=(2*xmlparser.maxm_diameter+4);j++)
                grid[i][j]=-1;
        }
    }
   //end of making grid

    @Override
    public void mouseClicked(MouseEvent e) {
       int x= e.getX();
       int y=e.getY();
       j= x/unit_width;
       i= y/unit_height;
       //JOptionPane.showMessageDialog(this, "i "+i+" j ="+j);
       if(grid[i][j]!=-1)
       {
           node_number= grid[i][j];
           String tagdata=xmlparser.tag_obj[node_number].tag_data;
           if(tagdata==" ")
               tagdata="NULL";
           String tagatt=xmlparser.tag_obj[node_number].tag_attribute;
           if(tagatt==" ")
               tagatt="NULL";
           String str= "Tag Name : "+xmlparser.tag_obj[node_number].tag_name+"\n Tag Attribute: "+tagatt+"\n Tag Data: "+tagdata+"\n";
           JOptionPane.showMessageDialog(this, str);
       }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   
}
    