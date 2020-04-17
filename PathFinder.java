import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.Scanner;

public class PathFinder
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Hello there!");
        int xi,xf,yi,yf;
        
        world World = new world(8,8);

        Shape shape = new Shape();
        pickAndFill(shape);

        int x = 1 + (int)(Math.random() * (7 - shape.getWidth()));
        int y = 1 + (int)(Math.random() * (7 - shape.getHeight()));
        World.setObstacle(shape, x, y);
        
        World.print();

        do{
            System.out.println("Enter the x coordinate of the initial point(1-8)");
            xi = input.nextInt();
            System.out.println("Enter the y coordinate of the initial point(1-8)");
            yi = input.nextInt();
            System.out.println("Enter the x coordinate of the final point(1-8)");
            xf = input.nextInt();
            System.out.println("Enter the y coordinate of the final point(1-8)");
            yf = input.nextInt();
            System.out.println();
        }while(!(World.setInitialPoint(xi, yi) && World.setFinalPoint(xf, yf)));
        
        System.out.println("Inputs accepted!");
        input.close();
    }
    
    public static void pickAndFill(Shape shape)
    {
        int r = (int) (Math.random() * 3);
        boolean[][] filledBlocks = null;
        shape.setWidth(3);
        shape.setHeight(3);
        if(r == 0)//shapeT
        {
            filledBlocks = new boolean[3][3];
            for(int i = 0; i < shape.getHeight(); i++)
                for(int j = 0; j < shape.getWidth(); j++)
                    filledBlocks[i][j] = false;

            filledBlocks[0][0] = true;
            filledBlocks[0][1] = true;
            filledBlocks[0][2] = true;
            filledBlocks[1][1] = true;
            filledBlocks[2][1] = true;
        }
        else if(r == 1)//shapeI
        {
            filledBlocks = new boolean[3][3];
            for(int i = 0; i < shape.getHeight(); i++)
                for(int j = 0; j < shape.getWidth(); j++)
                    filledBlocks[i][j] = false;
            
            filledBlocks[0][0] = true;
            filledBlocks[0][1] = true;
            filledBlocks[0][2] = true;
            filledBlocks[1][1] = true;
            filledBlocks[2][0] = true;
            filledBlocks[2][1] = true;
            filledBlocks[2][2] = true;
        }
        else//shapeL
        {
            filledBlocks = new boolean[3][3];
            for(int i = 0; i < shape.getHeight(); i++)
                for(int j = 0; j < shape.getWidth(); j++)
                    filledBlocks[i][j] = false;
            
            filledBlocks[0][0] = true;
            filledBlocks[1][0] = true;
            filledBlocks[2][0] = true;
            filledBlocks[2][1] = true;
            filledBlocks[2][2] = true;
        }
        shape.setBlockade(filledBlocks);
    }
}

class world
{
    public cell[][] Cells;
    public cell initialPoint;
    public cell finalPoint;
    
    public world(int height, int width)
    {
        Cells = new cell[height][width];
        
        for(int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Cells[i][j] = new cell((char)(i+65) + Integer.toString(j),new int[]{i, j});
            }
        }

        for(int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                if(i != 0)  Cells[i][j].addNeighbour(Cells[i-1][j]);
                if(i != 7)  Cells[i][j].addNeighbour(Cells[i+1][j]);
                if(j != 0)  Cells[i][j].addNeighbour(Cells[i][j-1]);
                if(j != 7)  Cells[i][j].addNeighbour(Cells[i][j+1]);
            }
        }
    }

    public void print()
    {
        for(int i = 0; i < Cells.length; i++)
        {
            for (cell c : Cells[i])
            {
                if(c.isObstacle())
                    System.out.print("__ ");
                else
                    System.out.print(c.getName()  + " ");
            }
            System.out.println();
        }
    }
    
    public void setObstacle(Shape s, int x, int y)
    {
        boolean blocks[][] = s.getBlocks();
        for(int i = 0; i < s.getHeight(); i++)
            for(int j = 0; j < s.getWidth(); j++)
            {
                if(blocks[i][j])
                    Cells[i+y][j+x].makeObstacle();
            }
    }
    
    public boolean setInitialPoint(int x, int y)
    {
        if(x > Cells.length || x < 1)
        {
            System.out.printf(" The given point, {%2s,%2s} is out of bounds.\n\n",x,y);
            return false;
        }
        if(y > Cells[0].length || y < 1)
        {
            System.out.printf(" The given point, {%2s,%2s} is out of bounds.\n\n",x,y);
            return false;
        }
        if(Cells[x - 1][y - 1].isObstacle())
        {
            System.out.printf(" The given point, {%2s,%2s} is an obstacle.\n\n",x,y);
            return false;
        }    

        initialPoint = Cells[x - 1][y -1];
        return true;
    }
    
    public boolean setFinalPoint(int x, int y)
    {
        
        if(x > Cells.length || x < 1)
        {
            System.out.printf(" The given point, {%2s,%2s} is out of bounds.\n\n",x,y);
            return false;
        }
        if(y > Cells[0].length || y < 1)
        {
            System.out.printf(" The given point, {%2s,%2s} is out of bounds.\n\n",x,y);
            return false;
        }
        if(Cells[x - 1][y - 1].isObstacle())
        {
            System.out.printf(" The given point, {%2s,%2s} is an obstacle.\n\n",x,y);
            return false;
        }    
        
        finalPoint = Cells[x - 1][y -1];
        return true;
    }
    
    public cell getInitialPoint()
    {
        return initialPoint;
    }
    
    public cell getFinalPoint()         
    {
        return finalPoint; 
    }
}

class cell
{
    private String name;
    private Vector<cell> neighbours;
    private String status;
    private int coordinate[];

    public cell(String str, int arr[])
    {
        coordinate = arr.clone();
        neighbours = new Vector<cell>();
        name = str;
        status = "open";
    }

    public String getName()                         {return name;}

    public Vector<cell> getNeighbours()             {return neighbours;}

    public String getStatus()                       {return status;}

    public int[] getCoordinates()                   {return coordinate;}

    public void setNeighbours(Vector<cell> vec)     {neighbours = new Vector<cell>(vec);}

    public void setStatus(String str)               {status = str;}

    public void addNeighbour(cell c)                {neighbours.add(c);}

    public void makeObstacle()
    {
        for(cell c : neighbours)
            c.removeNeighbour(this);
        neighbours = null;
    }

    public boolean isObstacle()                     {return neighbours == null;}

    public void removeNeighbour(cell c)             {neighbours.remove(c);}

    public int getManhattanDistance(cell c)         
    {
        return Math.abs(c.coordinate[0] - this.coordinate[0]) + Math.abs(c.coordinate[1] - this.coordinate[1]);
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
        blocks = new boolean[height][width];
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                blocks[i][j] = filledBlocks[i][j];
            }
        }
    }

    public boolean[][] getBlocks()
    {
        return blocks;
    }
}