package libsvm;
public class svm_problem implements java.io.Serializable
{
	/**
	 * 训练样本中，样本的标签，即第l个训练样本
	 */
	public int l;
	/**
	 * 训练样本的目标变量Y
	 */
	public double[] y;
	/**
	 * 训练样本的自变量X
	 */
	public svm_node[][] x;
}
