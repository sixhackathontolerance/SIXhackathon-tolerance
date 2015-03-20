package six.tolerance.check;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class UserRecomendation {

	public static void main(String[] args) throws IOException {
		DataModel datamodel = new FileDataModel(new File("data/news_stream.csv"));
	}

}
