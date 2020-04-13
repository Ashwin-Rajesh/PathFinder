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

        Shape shape = new Shape();
        pickAndFill(shape);

        int x = 1 + (int)(Math.random() * (7 - shape.getWidth()));
        int y = 1 + (int)(Math.random() * (7 - shape.getHeight()));
        World.setObstacle(shape, x, y);
        World.print();
    }
    
    public static void pickAndFill(Shape shape)
    {
        int r = (int) (Math.random() * 3);
        boolean[][] filledBlocks = null;
        if(r == 0)//shapeT
        {
            shape.setWidth(3);
            shape.setHeight(3);
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
            shape.setWidth(3);
            shape.setHeight(3);
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
            shape.setWidth(3);
            shape.setHeight(3);
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
                    System.out.print(c.getName() + " ");
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
}

class cell
{
    private String name;
    private Vector<cell> neighbours;
    private String status;

    public cell(String str)
    {
        neighbours = new Vector<cell>();
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

    public void addNeighbour(cell c)
    {
        neighbours.add(c);
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

    public void makeObstacle()
    {
        for(cell c : neighbours)
            c.removeNeighbour(this);
        neighbours = null;
    }

    public boolean isObstacle()
    {
        return neighbours == null;
    }

    public void removeNeighbour(cell c)
    {
        neighbours.remove(c);
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