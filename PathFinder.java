import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Vector;

public class PathFinder
{
    public static void main(String[] args)
    {
        System.out.println("Hello there!");
        world World = new world(5,5);
        World.print();
    }
}

class world
{
    public cell[][] Cells;

    public world(int height, int width)
    {
        Cells = new cell[height][width];
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                Cells[i][j] = new cell(Integer.toString(i)+","+Integer.toString(j));
    }

    public void print()
    {
        for(int i = 0; i < Cells.length; i++)
        {
            for(cell c : Cells[i])
                System.out.print(c.getName() + " ");
            System.out.println();
        }
    }
}

class cell
{
    private String name;
    private Vector<cell> neighbours;

    public cell(String str)
    {
        name = str;
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
}