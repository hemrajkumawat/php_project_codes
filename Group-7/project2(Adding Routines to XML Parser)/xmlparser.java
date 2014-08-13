package xml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;
class tag_struct
{
    public String tag_attribute ;
    public String tag_attribute1[]=new String [100] ;
    public String tag_attribute_value[]=new String [100] ;
    public String tag_data;
    public String tag_name;
    public Integer parent_name;
    public int node_index;
    public int level_number;
    public Vector<Integer>child= new Vector<>();
    public tag_struct()
    {
       // this.tag_attribute="";
        this.tag_data="";
        this.tag_name="";
        this.parent_name=0;
        this.node_index=0;
        this.level_number=0;
    }
}
public class xmlparser 
{
     public static int line_num=1;
     public static int max_depth=-1;
     public static int parent_node_number=0;
     public static int line_length;
     public static boolean error_check= false;
     public static String closing_tag="";
     public static int maxm_diameter=0;
     public static String string_between_tags="", parent_name="";
     public static String attribute_string="";
     public static Stack <String> tag_stack= new Stack<>();
     public static Stack <Integer> tag_number= new Stack<>();
     public static tag_struct tag_obj[]=new tag_struct[10000];
     public static Integer level_diameter[]= new Integer[1000]; 
     static InputStream is;
     public static  String att_string[] = new String[100];
     public static int no_of_att=0;
     public static int no_of_att1=0;
     public static Stack <tag_struct> tree_data= new Stack<>(); // it will store root parent and child relationship
     public static int node_number =-1;
     public static void main(String args[])  throws IOException,FileNotFoundException    
     {
           for(int i=0;i<10000;i++)
           {
              tag_obj[i]=new tag_struct();
           }
        BufferedReader br;
      
       br= new BufferedReader(new FileReader("C:\\Users\\Mahesh\\workspace\\xml_read\\src\\read.xml"));
        if(br==null)
        {
            System.out.println("file not found Exception");
            System.exit(0);
        }
        String str,tag_string,note=null;
        int i;
        
        string_between_tags="";
        while( (str= br.readLine())!=null)
        {
            if(error_check==true)// if there is error then break reading the file
                break;
            
            line_num++;
            line_length = str.length();
            
            
            for(i=0;i<line_length&&error_check==false;)
            {
                if(str.charAt(i)=='<' && (str.charAt(i+1)=='?' || str.charAt(i+1)=='!')) 
                    break;
                
                tag_string="";
                if(str.charAt(i)=='<') // char_data[i+1] is checking for comment part
                {
                    i++;
                    while( i<line_length && str.charAt(i)!='>')
                    {
                        tag_string+=  str.charAt(i); i++;
                    }
                     get_tagword(tag_string); // the string between < and > sign is send to this function 
                     i++;
                }
                else
                {
                    while(i<line_length &&  str.charAt(i)!='<')
                    {
                        if(node_number>=0)tag_obj[node_number].tag_data+=str.charAt(i);
                         string_between_tags+=  str.charAt(i); i++;
                         
                    }
                    
                }
            }
        }
 
            
            if( error_check== false && !tag_stack.empty()) // if all tags are not popped from the stack , i.e. some of them are not matched
            {
                System.out.println("Opening Tag of "+ closing_tag+ " is not found");
                System.out.println("The file is error. Some tags not closed");
            }
             
            if(error_check== true) // there is error in the xml file
            {
                System.out.println("\nThe file contains some error. It is not well formed.");
            }
            else 
            {
              // print_parent_child_relation();
               System.out.println("\nThe file doesn't contain any error. It is well formed.");  
               
             //  print_parent_child_relation();
             
               System.out.print( "Enter the integer no which you want to find \n" );
               System.out.print("1.getChildElement(parent node) \n");
               System.out.print("2.getElementContent( Element)\n");
               System.out.print("3.getListOfAttributes(Node name)\n");
               System.out.print("4.getValueOfAttributes(Attribute name)\n");
               System.out.println("\tNo child  "+ tag_obj[1].tag_attribute1[2]);
               Scanner scanner = new Scanner( System.in );
               String input = scanner.nextLine();
              int val;
              val = Integer.parseInt(input);
             
              if(val==1){
            	  System.out.print( "Enter the node : \n" );
            	  Scanner scanner1 = new Scanner( System.in );
                  String input1 = scanner1.nextLine();
              
               get_Child_Eelements(input1);}
              if(val==2){
            	  System.out.print( "Enter the node : \n" );
            	  Scanner scanner2 = new Scanner( System.in );
                  String input2 = scanner2.nextLine();
              
                 get_content_of_node(input2);}
              if(val==3){
            	  System.out.print( "Enter the node : \n" );
            	  Scanner scanner3 = new Scanner( System.in );
                  String input3 = scanner3.nextLine();
              get_list_of_attribute(input3);
              }
              if(val==4){
            	  System.out.print( "Enter the node : \n" );
            	  Scanner scanner4 = new Scanner( System.in );
                  String input4 = scanner4.nextLine();
              get_value_of_attribute(input4);
              } 
              
              
              
              
            }
    }// closing of main function
    
    public static void get_tagword(String tag)// for getting opening and closing tag words
    {
        int i=0,counter=0;
        int l= tag.length();
        boolean space_error_check;
        String tag_string, stack_top_string , xml_tag_name;
        
    
        if(l==0)// if the tag is empty
        {
             System.out.println("The tag should not be null ");
             System.out.println("The file contains error in line no. "+(line_num-1));
             error_check= true;
             return;
        }
        
        if(tag.charAt(0)==' ' || tag.charAt(l-1)==' ') // contains error
        {
             System.out.println("The tag "+tag+" should not contain empty space ");
             System.out.println("The file contains error in line no. "+(line_num-1));
             error_check=true; 
             return;
        }
        
        
        
        
        if(tag.charAt(0)!='/') // opening tag
        {
            node_number++;
            if(l>=3) // starting 3 characters should not be "xml"
            {
                xml_tag_name= tag.substring(0,3);
                if(xml_tag_name.equals("xml")==true)
                {
                    System.out.println("The tag "+tag+" should not start with xml ");
                    System.out.println("The file contains error in line no. "+(line_num-1));
                     error_check=true;
                     return;
                }
                
            }
           if(tag.charAt(0)>='a' && tag.charAt(0)<='z' || tag.charAt(0)>='A' && tag.charAt(0)<='Z' || tag.charAt(0)=='_') // starting char should not be digit
            {
               
           space_error_check= check_for_space_error(tag); // checks for forbidden space in the tag string, define below
            if(space_error_check==true)// there is error
            {
                error_check= true;
                return;
            }
             
            if(tag.charAt(l-1)=='/')
            {
                return;
            }
            while(i<l && tag.charAt(i)!=' ' )
            {
                i++;
                counter++;
            }
            
            tag_string = tag.substring(0, counter);
            tag_stack.push(tag_string);
            tag_number.push(node_number);
             
            if(counter!=l) // for checking attributes
            {
            	
            		
               attribute_string= tag.substring(counter+1,l);
                 find_Attribute_And_Value(attribute_string, node_number);
            }
            if(attribute_string.equals(""))
                    attribute_string="NULL";
            tag_obj[node_number].tag_name= tag_string;
          //  tag_obj[node_number].tag_attribute= attribute_string;
            tag_obj[node_number].node_index= node_number;
           }// opening <tag> checked end
           else
           {
                System.out.println("The tag "+tag+" should not start with a a special character or digit ");
                 System.out.println("The file contains error in line no. "+(line_num-1));
                error_check= true;
                return;
           }
               
        } // end of opening tag
        
        
        
        else
        {
            while(i<l && tag.charAt(i)!=' ' )
            {
                i++;
                counter++;
            }   
            closing_tag = tag.substring(1,l); // closing tag attributes
            stack_top_string= tag_stack.peek();
            if( tag_stack.empty()== false && (tag_stack.peek()).equals(closing_tag) )// if stack is not empty and it is matched
            {
                tag_stack.pop();
                int temp1= tag_number.peek(); // child node number
                tag_number.pop();
                if(tag_stack.size()>0)
                {
                    parent_name= tag_stack.peek(); 
                    int temp= tag_number.peek(); // parent node number
                    int nod= tag_obj[temp1].node_index;
                  //  System.out.println("adfdd"+nod);
                    tag_obj[temp1].parent_name= temp;
                    tag_obj[temp].child.add(temp1);
                    //System.out.println("Tag Number:"+tag_obj[temp1].node_index+" Tag Name:"+closing_tag+"\tTag's Attribute:"+attribute_string+"\tTag Data:"+string_between_tags+"\tParent:"+ parent_name);
                }
                else 
                {
                     parent_name= "ROOT NODE";
                      int nod= tag_obj[0].node_index;
                     tag_obj[nod].parent_name= 0;
                   // System.out.println("Tag Number:"+tag_obj[0].node_index+"Tag Name:"+closing_tag+"\tTag's Attribute:"+ attribute_string+"\tTag Data:"+ string_between_tags+"\tParent:"+ parent_name);
                }
                string_between_tags="";
                attribute_string="";
            }
            else
            {
                error_check=true; // errorneous code
                System.out.println("the opening tag of "+closing_tag+" is not matched\n");
                System.out.println("The file contains error in line no. "+(line_num-1));
                return;
            }       
       }// closing tag ended
    }    // function get_tagword ended here

       // function check_for_space_error for space error checking starts here
    
    public static boolean check_for_space_error(String tag) 
    {
       int l= tag.length(),i;
       tag+='>';
       int quote_counter=0,num_of_spaces=0;
       for(i=0;i<l;i++)
       {
           if(tag.charAt(i)==' ' && quote_counter%2==0)
               num_of_spaces++;
           if(tag.charAt(i)==' ' && quote_counter%2==0 && tag.charAt(i+1)==' ')//continous space checking
            {
                error_check=true; // errorneous code
                System.out.println("There are more than one space between two words in the tag strings: "+tag+"\n");
                System.out.println("The file contains error in line no. "+(line_num-1));
                return true;
            }
           if(tag.charAt(i)=='"')
           {
               quote_counter++;
               if(quote_counter%2==0) // even  means the closing quote
               {
                    if(tag.charAt(i+1)!='>' && tag.charAt(i+1)!=' ') // space between multiple strings
                    {
                        error_check=true; // errorneous code
                        System.out.println("There are no space between multiple attributes of the tag : "+tag+"\n");
                        System.out.println("The file contains error in line no. "+(line_num-1));
                        return true;
                    }
               }
           }
       }// end of for loop
         if(quote_counter!=2*num_of_spaces) // if attributes quotes are not matched
           {
                error_check=true; // errorneous code
                System.out.println("The double quotes of the attributes of the tag "+tag+" are not matched \n");
                System.out.println("The file contains error in line no. "+(line_num-1));
                return true;
           }       
         return false;
    } // check for space error in the tag string i.e. < and > sign

    // printing the parent child relationship
    
    public static void   get_content_of_node(String node)
    {
    	int x=0;
 	   for(int idx=0;idx<=node_number;idx++)
 	   {  
 		   if((tag_obj[idx].tag_name).equals(node) ){
 			  x=idx; 
 		   }
 		   else continue;    
 		   
 	   }
 	   
 	
 	if(tag_obj[x].child.size()>0)
                {
                   
                    for(int v=0; v<tag_obj[x].child.size();v++)
                    {
                 	   int y=v+1;
                        int ch=tag_obj[x].child.get(v);
                       String  pa= tag_obj[ch].tag_data;
                       // tag_obj[ch].level_number= tag_obj[pa].level_number+1; 
                        System.out.print("       "  + pa+"  \n");
                    }
                    System.out.println();
                }
               
                else System.out.println("\tNo child");
               // max_depth= Math.max(max_depth,tag_obj[idx].level_number);
    
    
    
    }
    public static void  get_value_of_attribute(String att_name)
    {
    	for(int x=0;x<=node_number; x++)
    	    {
    	 for(int v=1;v<=tag_obj[x].tag_attribute1.length;v++)
    	    {
    		 System.out.print(" Attribute: "+tag_obj[x].tag_attribute_value[v]+"  "  +att_name +"  \n");
    		// if((tag_obj[x].tag_attribute1[v]).equals(att_name) ){
    		//	 System.out.println(att_name+"   "+ tag_obj[x].tag_attribute_value[v]);
    			 
    		 //}
    	    }
    
    	    }
    
    }
    public static void  get_list_of_attribute(String node_name)
    {
    	int x=0;
 	   for(int idx=0;idx<=node_number;idx++)
 	   {  
 		   if((tag_obj[idx].tag_name).equals(node_name) ){
 			  x=idx; 
 		   }
 		   else continue;    
 		   
 	   }
 	//  System.out.print(" Attribute: "+tag_obj[x].tag_attribute1.length+"    \n");
    for(int v=1;v<=tag_obj[x].tag_attribute1.length;v++)
    {
    	
    	String at=tag_obj[x].tag_attribute1[v];
    	if(at!=null){
    	 System.out.print(" Attribute:   "  + at+"  \n");}
    	
    }
      
    } 
    public static void find_Attribute_And_Value( String att_string1, int node_no ){                     
    	int l= att_string1.length(),x,i,j,no_of_att1=1;
    	  for(i=0;i<=l;i++)
    		  
    	  {       
    		              if(att_string1.charAt(i)!=' '&& att_string1.charAt(i)!='"' && att_string1.charAt(i)!='='){
    			                   for(j=i;j<=l;j++){
    				                     if(att_string1.charAt(j)=='='){ 
    				                    	 no_of_att++; 
    				             	  att_string[no_of_att]= att_string1.substring(i,j);
    				             	   tag_obj[node_no].tag_attribute1[no_of_att]=  att_string[no_of_att] ;
    					              
    					                 i=j;
    					             //    System.out.println("\tNo child  "+ tag_obj[node_no].tag_attribute1[no_of_att]+node_no);
    					                
    					               break;
    					                  } 
    				                
    				                     }
    			                 
    			                   }
    				  if(att_string1.charAt(i)=='"')
    				  {
    					  for(j=i+1;j<=l;j++){
    	    				  if(att_string1.charAt(j)=='"'){ 
    	    					  no_of_att1++;
    	    					  att_string[no_of_att1]= att_string1.substring(i+1,j);
    	    					   tag_obj[node_no].tag_attribute_value[no_of_att1]=  att_string[no_of_att1] ;
    	    				//	  System.out.println("\tNo child  "+att_string[no_of_att1]+  tag_obj[node_no].tag_attribute_value[no_of_att1]);
    	    					  i=j+1;
    	    					  break;
    				             }
    	    				//  System.out.println("nb"+i);
    					  }
    					//  System.out.println("vc"+i);
    				  }
    				  else continue;
    	  }
    	  
    }
    public static void get_Child_Eelements( String node) throws IOException 
    {
    	int x=0;
    	   for(int idx=0;idx<=node_number;idx++)
    	   {  
    		   if((tag_obj[idx].tag_name).equals(node) ){
    			  x=idx; 
    		   }
    		   else continue;    
    		   
    	   }
    	   
    	
    	if(tag_obj[x].child.size()>0)
                   {
                      
                       for(int v=0; v<tag_obj[x].child.size();v++)
                       {
                    	   int y=v+1;
                           int ch=tag_obj[x].child.get(v);
                          String  pa= tag_obj[ch].tag_name;
                          // tag_obj[ch].level_number= tag_obj[pa].level_number+1; 
                           System.out.print(" Child: "+y+"  "  + pa+"  \n");
                       }
                       System.out.println();
                   }
                  
                   else System.out.println("\tNo child");
                  // max_depth= Math.max(max_depth,tag_obj[idx].level_number);
    	
    }
   public static void print_parent_child_relation() throws IOException 
    {
         for(int idx=0;idx<=node_number;idx++)
         {
              //     System.out.print("Node_number: "+tag_obj[idx].node_index+"\tTag_name : "+tag_obj[idx].tag_name+"\tTag attribute:"+tag_obj[idx].tag_attribute+"\t Tag Data:"+tag_obj[idx].tag_data+"\tParent Name:"+tag_obj[idx].parent_name+" Level Number:"+tag_obj[idx].level_number );
                   if(tag_obj[idx].child.size()>0)
                   {
                       System.out.print("\tChild: ");
                       for(int v=0; v<tag_obj[idx].child.size();v++)
                       {
                           int ch=tag_obj[idx].child.get(v);
                           int pa= tag_obj[ch].parent_name;
                           tag_obj[ch].level_number= tag_obj[pa].level_number+1; 
                           System.out.print(" "+ch+"  ");
                       }
                       System.out.println();
                   }
                  
                   else System.out.println("\tNo child");
                   max_depth= Math.max(max_depth,tag_obj[idx].level_number);
         }
          
         for(int idx=0;idx<=max_depth+1;idx++)
         {
               level_diameter[idx]=0;
         }
         System.out.println("node number = "+node_number);
         for(int idx=0;idx<=node_number;idx++)
         {
              int child_level= tag_obj[idx].level_number;
               level_diameter[child_level]++;     
         }
         maxm_diameter=-1;
         for(int idx=0;idx<=max_depth;idx++)
         {
               maxm_diameter= Math.max(level_diameter[idx],maxm_diameter);
         }
        
      // System.out.println("Maxm depth ="+max_depth+" and maxm diameter= "+xmlparser.maxm_diameter);
      //  gui_tree gui_test_buildTree= new gui_tree();
    }
} 
