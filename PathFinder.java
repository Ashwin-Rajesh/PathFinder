import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Vector;

public class PathFinder
{
    public static void main(String[] args)
    {
        System.out.println("Hello there!");
        world World = new world(8,8);
        World.print();
        Shape shape = new Shape();
        pickAndFill(shape);
    }
    
    public static void pickAndFill(Shape shape)
    {
        int r = (int) (Math.random() * 3);
        boolean[][] filledBlocks;
        if(r == 0)//shapeT
        {
            shapeT(shape, filledBlocks);
        }
        else if(r == 1)//shapeI
        {
            shapeI(shape, filledBlocks);
        }
        else//shapeL
        {
            shapeL(shape, filledBlocks);
        }
        shape.setBlockade(filledBlocks);
    }
    
    public static void shapeT(Shape shape, boolean[][] filledBlocks)
    {
        int size = (int) (Math.random() * 3);
        shape.setWidth(3);
        if(size == 1)       //blocks = 4
        {
            shape.setHeight(2);
            filledBlocks = new boolean[shape.getHeight()][shape.getWidth()];
            filledBlocks[1][1] = true;
        }
        else                //blocks = 5
        {
            shape.setHeight(3);
            filledBlocks = new boolean[shape.getHeight()][shape.getWidth()];
            filledBlocks[1][1] = true;
            filledBlocks[2][1] = true;
        }
        for(int i = 0; i < shape.getWidth(); i++)
        {
            filledBlocks[0][i] = true;
        }
    }
    
    public static void shapeI(Shape shape, boolean[][] filledBlocks)
    {
        int size = (int) (Math.random() * 3);
        shape.setWidth(1);
        if(size == 0)       //blocks = 3
        {
            shape.setHeight(3);
            filledBlocks = new boolean[shape.getHeight()][shape.getWidth()];
        }
        else if(size == 1)  //blocks = 4
        {
            shape.setHeight(4);
            filledBlocks = new boolean[shape.getHeight()][shape.getWidth()];
        }
        else                //blocks = 5
        {
            shape.setHeight(5);
            filledBlocks = new boolean[shape.getHeight()][shape.getWidth()];
        }
        for(int i = 0; i < shape.getHeight(); i++)
        {
            filledBlocks[i][0] = true;
        }
    }
    
    public static void shapeL(Shape shape, boolean[][] filledBlocks)
    {
        int size = (int) (Math.random() * 2);
        shape.setHeight(3);
        if(size == 0)   // blocks = 4
        {
            shape.setWidth(2);
            filledBlocks = new boolean[shape.getHeight()][shape.getWidth()];
            filledBlocks[2][1] = true;
        }
        else            //blocks = 5
        {
            shape.setWidth(3);
            filledBlocks = new boolean[shape.getHeight()][shape.getWidth()];
            filledBlocks[2][1] = true;
            filledBlocks[2][2] = true;
        }
        for(int i = 0; i < shape.getHeight(); i++)
        {
            filledBlocks[i][0] = true;
        }
    }
}

class world
{
    public cell[][] Cells;

    public world(int height, int width)
    {
        Cells = new cell[height][width];
        
        for(int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Cells[i][j] = new cell((char)(i+65) + Integer.toString(j));
            }
        }
    }

    public void print()
    {
        for(int i = 0; i < Cells.length; i++)
        {
            for (cell c : Cells[i])
            {
                System.out.print(c.getName() + " ");
            }
            System.out.println();
        }
    }
    
}

class cell
{
    private String name;
    private Vector<cell> neighbours;
    private String status;

    public cell(String str)
    {
        name = str;
        status = "open";
    }

    public String getName()
    {
        return name;
    }

    public Vector<cell> getNeighbours()
    {
        return neighbours;
    }

    public void setNeighbours(Vector<cell> vec)
    {
        neighbours = new Vector<cell>(vec);
    }

    public void setStatus(String str)
    {
        status = str;
    }

    public String getStatus()
    {
        return status;
    }
}

class Shape
{
    private int height;
    private int width;
    private boolean[][] blocks; 
    
    public void setHeight(int height)
    {
        this.height = height;
    }
    
    public void setWidth(int width)
    {
        this.width = width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public void setBlockade(boolean[][] filledBlocks)
    {
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                blocks[i][j] = filledBlocks[i][j];
            }
        }
    }
}   
