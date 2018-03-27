import java.util.Arrays;
class BNode implements Comparable//Every key Pointer pair in each node of Btree
{
	Node left;
	Node right;
	key k;
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		BNode b = (BNode)o;
		return k.compareTo(b.k);
	
	}
	
}
class Node
{
BNode keys[] = new BNode[9];
Node par;
int ptr;
int n=0;
boolean isleaf;

}
class key implements Comparable
{
	String ValidT, InsID, InsName,Dept;
	int Salary;
	public key(String ValidT, String InsId,String InsName, String Dept,int Salary)
	{
		this.ValidT = ValidT;
		this.InsID = InsId;
		this.InsName = InsName;
		this.Dept = Dept;
		this.Salary = Salary;
		
	}
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		key k = (key)o;
		return InsID.compareTo(k.InsID);
		
	}
}
class Btree
{
	Node root = null;
	int order;
	public void insert(key k)
	{
		if(root == null)
		{
			root  = new Node();
			root.isleaf = true;
			root.keys[0] = new BNode();
			root.keys[0].k = k;
			root.n++;
		}
		else
		{
				Node L = new Node();
				L = findnode(k);
				System.out.println(L.n+" "+k.InsID);
				System.out.println(L.keys[0].k.InsID);
				if(L.n<order-1) // if Leaf not full
				{
					L.keys[L.n] = new BNode();
					L.keys[L.n].k = k;
					L.n++; // Insert the element and simply sort the leaf keys
					Arrays.sort(L.keys,0,L.n);
					if(L.par!=null && L.ptr <order-1 && L.par.keys[L.ptr]!=null)
					L.keys[order-2].right = L.par.keys[L.ptr].right;// the leaf should still keep pointing towards the adjacent leaf
					if(L.ptr ==order-1)
					{
						L.keys[order-2].right = null;
					}
				}
				else //Splitting if leaf node full
				{
					Node Lnew = new Node();//Making New Node
					Lnew.isleaf = true;//New Node will be leaf
					BNode temp[] = new BNode[order];//Temporary Node to keep overflow keys
					for(int i =0;i<order-1;i++)
					{
						temp[i] = L.keys[i]; // copying all values of L to temp 
					}
					temp[order-1] = new BNode();
					temp[order-1].k = k;// adding the last key which creates overflow
					Arrays.sort(temp);//sorting temp
					Lnew.keys[order-2] = new BNode();
					Lnew.keys[order-2].right = L.keys[order-2].right;//Setting the pointer of leaf node to adjacent node of L
					L.keys[order-2].right = Lnew;//Setting the pointer of L to Lnew 
					for(int i=0;i<order/2;i++)
					{
						L.keys[i] = temp[i];
					}
					L.n = (order/2);
					int p=0;
					for(int j=(order/2);j<order;j++)
					{
						Lnew.keys[p] = temp[j];
						p++;
					}
					Lnew.n = (order/2);
					ParentInsert(L,temp[(order/2)].k,Lnew);
					
				}
		}
	}
	
	public void ParentInsert(Node L,key k,Node Lnew)
	{
		if(L==root)
		{
			Node newroot = new Node();//Creating New root node
			newroot.keys[0] = new BNode();
			newroot.keys[0].k= k;
			newroot.n++;
			root = newroot;
			root.isleaf = false;
			root.keys[0].left = L;
			root.keys[0].right = Lnew;
			root.keys[1] = new BNode();
			root.keys[1].left = Lnew;
			L.par = root; //Setting new parent of L and Lnew
			L.ptr =0;
			Lnew.par = root;
			Lnew.ptr =1;
		}
		else
		{	
			Node par = L.par; // parent of L
			if(par.n<order-1) //if parent not full
		    {
			for(int i=par.n;i>L.ptr;i--)
			{
				par.keys[i]= par.keys[i-1];
				if(par.keys[i].left!=L)
				par.keys[i].left.ptr = i;
				if(par.keys[i].right!=L)
				par.keys[i].right.ptr = i+1;
			}
			//System.out.println(L.ptr);
			par.keys[L.ptr] = new BNode();
			par.keys[L.ptr].k = k; 
			par.keys[L.ptr].right = Lnew;
			
			if(L.ptr <order-2 && par.keys[L.ptr+1] == null )
				par.keys[L.ptr+1] = new BNode();
			if(L.ptr<order-2)
			par.keys[L.ptr+1].left = Lnew;
			par.keys[L.ptr].left = L;
			Lnew.par = par;
			Lnew.ptr = L.ptr+1;
			par.n++; 
			
		}
		else//split Parent if full
		{
			BNode temp[] = new BNode[order];
			for(int i =0;i<L.ptr;i++)
			{
				temp[i] = par.keys[i];
			}
			temp[L.ptr] = new BNode();
			temp[L.ptr].k = k;
			temp[L.ptr].right = Lnew;
			temp[L.ptr].left = L;
			
		    for(int i=L.ptr+1;i<order;i++)
			{
				temp[i] = par.keys[i-1];
			}
			if(L.ptr<order-1)
			{
			temp[L.ptr+1].left = Lnew;
			}
			Node Tnew = new Node();
			for(int i=0;i<(order-1)/2;i++)
			{
				par.keys[i] = temp[i];
			}
			par.n = (order-1)/2;
			for(int i=order/2;i<order;i++)
			{
				Tnew.keys[i-(order/2)] = temp[i];
				Tnew.keys[i-(order/2)].left.ptr = (i-(order/2));
				Tnew.keys[i-(order/2)].right.ptr = (i-(order/2)+1);
				Tnew.keys[i-(order/2)].left.par = Tnew;
				Tnew.keys[i-(order/2)].right.par = Tnew;
				
			}
			Tnew.n = (order/2);
			ParentInsert(par,temp[((order-1)/2)].k,Tnew);
			
		}
		}
			
	}
	public Node findnode(key k)//Finding the leaf Node where k can be inserted
	{
		
			int i = 0;
			Node val = root;
			while(val.isleaf!=true)
			{

				for(i=0;i<val.n;i++)
				{
					if(val.keys[i].k.InsID.compareTo(k.InsID)>=0)
					{
						break;//minimum key with ID greater or equal to value to be searched
					}
				}
				
				if(i==val.n)//No Such leaf found
				{
					val = val.keys[val.n-1].right;//point to last non-null leaf node
				}
				else if(k.InsID.equals(val.keys[i].k.InsID))//if equal value as value to be found
				{
					val = val.keys[i].right;
				}
				else
				{
					val = val.keys[i].left;
				}
			}
			return val;
		
	}
	public void Delete(key k)
	{
		Node L = findnode(k);
		
	}
	public void print(Node n)
	{
		int i =1;
		while(!n.isleaf)
		{
			System.out.println("Layer"+i);
			for(int j =0;j<n.n;j++)
			{
				System.out.print(n.keys[j].k.InsID+" ");
			}

		}
	}
}
public class Hw3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("E".compareTo("Ea"));
		key first = new key("1111","A","A","A",1234);
		key second = new key("1111","B","B","B",1234);
		key third = new key("1111","C","A","A",1234);
		key fourth = new key("1111","D","B","B",1234);
		key fifth= new key("1111","E","A","A",1234);
        key six = new key("1111","F","B","B",1234);
		//key seven = new key("1111","Cn","A","A",1234);
		//key eight = new key("1111","Z","B","B",1234);
		key nine = new key("1111","G","B","B",1234);
		key eleven = new key("1111","H","B","B",1234);
		//key ten = new key("1111","Freeq","B","B",1234);
		key twel = new key("1111","I","B","B",1234);
		key thirt = new key("1111","J","B","B",1234);
		key fourt = new key("1111","Da","B","B",1234);
		key fift =  new key("1111","Db","B","B",1234);
		key sixt = new key("1111","Ea","B","B",1234);
		key sevent =  new key("1111","Eb","B","B",1234);
		Btree obj = new Btree();
		obj.order = 4;
		obj.insert(first);
		obj.insert(second);
		obj.insert(third);
		obj.insert(fourth);
		obj.insert(fifth);
		obj.insert(six);
		//obj.insert(seven);
		//obj.insert(eight);
		//obj.insert(ten);
		obj.insert(nine);
		obj.insert(eleven);
		obj.insert(twel);
		obj.insert(thirt);
		obj.insert(fourt);
		obj.insert(fift);
		obj.insert(sixt);
		obj.insert(sevent);
		
		Node n = obj.root;
		n = n.keys[0].left.keys[1].left;
		//n = n.keys[8].right.keys[8].right.keys[8].right.keys[8].right;
		for(int i=0;i<n.n;i++)
		{
			System.out.print(n.keys[i].k.InsID+" ");
			
			//System.out.println();
		}
		
	
	}

}
