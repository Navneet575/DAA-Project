
import javax.swing.JOptionPane;
import java.util.*;
import java.io.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hp
 */
public class Problem3 extends javax.swing.JFrame {
    private static int nodeCount,edgeCount;

    /**
     * Creates new form Problem3
     */
    
    public class Kruskal {
//	public void main(String[] args) {
//		ArrayList<Edge> graphEdges = new ArrayList<Edge>();		//edge list, not adjacency list
//		graphEdges.add(new Edge(3, 5, 2));
//		graphEdges.add(new Edge(6, 7, 5));
//		graphEdges.add(new Edge(3, 4, 6));
//		graphEdges.add(new Edge(4, 8, 7));
//		graphEdges.add(new Edge(1, 2, 9));
//		graphEdges.add(new Edge(4, 5, 11));
//		graphEdges.add(new Edge(1, 6, 14));
//		graphEdges.add(new Edge(1, 7, 15));
//		graphEdges.add(new Edge(5, 8, 16));
//		graphEdges.add(new Edge(3, 6, 18));
//		graphEdges.add(new Edge(3, 8, 19));
//		graphEdges.add(new Edge(7, 5, 20));
//		graphEdges.add(new Edge(2, 3, 24));
//		graphEdges.add(new Edge(7, 8, 44));		//Edges created in almost sorted order, only the last 2 are switched but this is unnecessary as edges are sorted in the algorithm
//		graphEdges.add(new Edge(6, 5, 30));
//		//how many nodes. NODE COUNT MUST BE ENTERED MANUALLY. No error handling between nodeCount and graphEdges
//
//		Kruskal graph = new Kruskal();	//CAREFUL: nodeCount must be correct. No error checking between nodeCount & graphEdges to see how many nodes actually exist
//		graph.kruskalMST(graphEdges, nodeCount);
//	}

	public void kruskalMST(ArrayList<Edge> graphEdges, int nodeCount){
		//String outputMessage="";

		Collections.sort(graphEdges);		//sort edges with smallest weight 1st
		ArrayList<Edge> mstEdges = new ArrayList<Edge>();	//list of edges included in the Minimum spanning tree (initially empty)

		DisjointSet nodeSet = new DisjointSet(nodeCount+1);		//Initialize singleton sets for each node in graph. (nodeCount +1) to account for arrays indexing from 0

		for(int i=0; i<graphEdges.size() && mstEdges.size()<(nodeCount-1); i++){		//loop over all edges. Start @ 1 (ignore 0th as placeholder). Also early termination when number of edges=(number of nodes-1)
			Edge currentEdge = graphEdges.get(i);
			int root1 = nodeSet.find(currentEdge.getVertex1());		//Find root of 1 vertex of the edge
			int root2 = nodeSet.find(currentEdge.getVertex2());
			//outputMessage+="find("+currentEdge.getVertex1()+") returns "+root1+", find("+currentEdge.getVertex2()+") returns "+root2;		//just print, keep on same line for union message
			//String unionMessage=",\tNo union performed\n";		//assume no union is to be performed, changed later if a union DOES happen
			if(root1 != root2){			//if roots are in different sets the DO NOT create a cycle
				mstEdges.add(currentEdge);		//add the edge to the graph
				nodeSet.union(root1, root2);	//union the sets
			//	unionMessage=",\tUnion("+root1+", "+root2+") done\n";		//change what's printed if a union IS performed
			}
			//outputMessage+=unionMessage;
		}

		//outputMessage+="\nFinal Minimum Spanning Tree ("+mstEdges.size()+" edges)\n";
                jTextArea2.append("Final Minimum Spanning Tree \n");
                jTextArea2.append("Edge\tWeight\n");
		int mstTotalEdgeWeight=0;
		for(Edge edge: mstEdges){
			jTextArea2.append(edge +"\n");		//print each edge
			mstTotalEdgeWeight += edge.getWeight();
		}
		jTextArea2.append("\nTotal weight of all edges in MST = "+mstTotalEdgeWeight);

		//System.out.println(outputMessage);

//		try (PrintWriter outputFile = new PrintWriter( new File("06outputMST.txt") ); ){
//			outputFile.println(outputMessage);
//			System.out.println("\nOpen \"06outputMST.txt\" for backup copy of answers");
//		} catch (FileNotFoundException e) {
//			System.out.println("Error! Couldn't create file");
//		}
	}
}


class Edge implements Comparable<Edge>{
	private int vertex1;	//an edge has 2 vertices & a weight
	private int vertex2;
	private int weight;

	public Edge(int vertex1, int vertex2, int weight){
		this.vertex1=vertex1;
		this.vertex2=vertex2;
		this.weight=weight;
	}

	public int getVertex1(){
		return vertex1;
	}

	public int getVertex2(){
		return vertex2;
	}

	public int getWeight(){
		return weight;
	}

	@Override
	public int compareTo(Edge otherEdge) {				//Compare based on edge weight (for sorting)
		return this.getWeight() - otherEdge.getWeight();
	}

	@Override
	public String toString() {
		return "("+getVertex1()+", "+getVertex2()+")\t"+getWeight();
	}
}


// DisjointSet class
//
// CONSTRUCTION: with int representing initial number of sets
//
// ******************PUBLIC OPERATIONS*********************
// void union(root1, root2) --> Merge two sets
// int find(x)              --> Return set containing x
// ******************ERRORS********************************
// No error checking is performed
// http://users.cis.fiu.edu/~weiss/dsaajava3/code/DisjSets.java

/**
 * Disjoint set class, using union by rank and path compression
 * Elements in the set are numbered starting at 0
 * @author Mark Allen Weiss
 */
class DisjointSet{
	private int[] set;		//the disjoint set as an array

	public int[] getSet(){		//mostly debugging method to print array
		return set;
	}

	/**
	 * Construct the disjoint sets object.
	 * @param numElements the initial number of disjoint sets.
	 */
	public DisjointSet(int numElements) {		//constructor creates singleton sets
		set = new int [numElements];
		for(int i = 0; i < set.length; i++){		//initialize to -1 so the trees have nothing in them
			set[i] = -1;
		}
	}

	/**
	 * Union two disjoint sets using the height heuristic.
	 * For simplicity, we assume root1 and root2 are distinct
	 * and represent set names.
	 * @param root1 the root of set 1.
	 * @param root2 the root of set 2.
	 */
	public void union(int root1, int root2) {
		if(set[root2] < set[root1]){		// root2 is deeper
			set[root1] = root2;		// Make root2 new root
		}
		else {
			if(set[root1] == set[root2]){
				set[root1]--;			// Update height if same
			}
			set[root2] = root1;		// Make root1 new root
		}
	}

	/**
	 * Perform a find with path compression.
	 * Error checks omitted again for simplicity.
	 * @param x the element being searched for.
	 * @return the set containing x.
	 */
	public int find(int x) {
		if(set[x] < 0){		//If tree is a root, return its index
			return x;
		}
		int next = x;		
		while(set[next] > 0){		//Loop until we find a root
			next=set[next];
		}
		return next;
	}
	
}
    
    public Problem3() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clearButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        enterButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        calculateButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("KRUSKALS MINIMAL SPANNING TREE");

        clearButton.setText("CLEAR");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        backButton.setText("BACK");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        exitButton.setText("EXIT");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("KRUSKALS MINIMAL SPANNING TREE");

        jLabel2.setText("Enter no. of nodes :");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Enter number of Edges : ");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        enterButton.setText("ENTER");
        enterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterButtonActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setEnabled(false);
        jScrollPane1.setViewportView(jTextArea1);

        calculateButton.setText("Calculate MST");
        calculateButton.setEnabled(false);
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Enter edges in format :");

        jLabel5.setText("node1   node2   weight");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setEnabled(false);
        jScrollPane2.setViewportView(jTextArea2);

        jLabel6.setText("The minimal Spanning tree is : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(clearButton)
                .addGap(146, 146, 146)
                .addComponent(backButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exitButton)
                .addGap(96, 96, 96))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(calculateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(204, 204, 204))
            .addGroup(layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jLabel3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                        .addComponent(enterButton)
                        .addGap(41, 41, 41))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(enterButton))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addGap(8, 8, 8)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addComponent(calculateButton)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearButton)
                    .addComponent(backButton)
                    .addComponent(exitButton))
                .addGap(48, 48, 48))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        jTextArea1.setText("");
        jTextArea2.setText("");
        jTextField1.setText("");
        jTextField2.setText("");
        jTextArea1.setEnabled(false);
        calculateButton.setEnabled(false);
        jTextArea2.setEnabled(false);
    }//GEN-LAST:event_clearButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
       System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void enterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterButtonActionPerformed
            
        try{
            int nodes = Integer.parseInt(jTextField1.getText());
            nodeCount = nodes;
            int edges = Integer.parseInt(jTextField2.getText());
            edgeCount = edges;
            jTextArea1.setEnabled(true);
            jTextArea1.setEditable(true);
            calculateButton.setEnabled(true);
        }
            catch(Exception e){
                   JOptionPane.showMessageDialog(this,"Please Enter a Valid Integer ! ","Error",JOptionPane.ERROR_MESSAGE);
                   jTextField1.setText("");
                   jTextField2.setText("");
            }
    }//GEN-LAST:event_enterButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
       MainFrame mainframe = new MainFrame();
        mainframe.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_backButtonActionPerformed

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
     try{ 
        int input[][] = new int [edgeCount][3];
         String rows[] = jTextArea1.getText().split("\n");
        for(int i=0;i<edgeCount;i++){
            String arr[] = rows[i].split(" ");
                    for(int j=0;j<3;j++){
                        input[i][j]= Integer.parseInt(arr[j]);
                    }
        }
        
        ArrayList<Edge> graphEdges = new ArrayList<Edge>();
        for(int i=0;i<edgeCount;i++){
            int node1,node2,weight;
            int j=0;
               node1 = input[i][j];
               node2 = input[i][++j];
               weight = input[i][++j];
            graphEdges.add(new Edge(node1, node2, weight));
        }
        Kruskal graph = new Kruskal();
        graph.kruskalMST(graphEdges, nodeCount);
        jTextArea2.setEnabled(true);
     }
        catch(Exception e){
                   JOptionPane.showMessageDialog(this,"Please Enter a Valid Graph ! ","Error",JOptionPane.ERROR_MESSAGE);
                   jTextArea1.setText("");
               }
    }//GEN-LAST:event_calculateButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Problem3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Problem3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Problem3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Problem3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Problem3().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton calculateButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton enterButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
