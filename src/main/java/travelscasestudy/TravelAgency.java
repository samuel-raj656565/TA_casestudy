package travelscasestudy;

import java.util.*;


import java.io.*;
import java.sql.*;
import java.sql.Statement;
class InvalidIdException extends Exception {
	InvalidIdException() {
		super("Invalid Id passed");
	}
}

public class TravelAgency {

	public List<TourPackage> generatePackageCost() throws InvalidIdException, SQLException {
		List<TourPackage> list = new ArrayList();
		int i = 1;
		Scanner sc = null;
		try {
			Connection cnn=database.establishConnection();
			
			
				PreparedStatement ps=cnn.prepareStatement("insert into Package_Details values(?,?,?,?,?)");
				
				PreparedStatement prs=cnn.prepareStatement("select * from Package_Details where pk_id=?");
				
			File file = new File("D://Package.txt");
			sc = new Scanner(file);
			while (sc.hasNextLine()) {

				String st = sc.nextLine();
				st = st.trim();
				String arr[] = st.split(",");

				String id = arr[0].trim();
				if (validate(id)) {
					String source_place = arr[1].trim();
					String destination_place = arr[2].trim();
					int days = Integer.parseInt(arr[3].trim());
					double basic_fare = Double.parseDouble(arr[4].trim());
					TourPackage P1 = new TourPackage(id, source_place, destination_place,days,
							basic_fare);
					P1.calculatePacakgeCost();
					list.add(P1);
					ps.setString(1, id);
					ps.setString(2,source_place);
					ps.setString(3,destination_place);
					ps.setInt(4,days);
					ps.setDouble(5,P1.package_cost);
					prs.setString(1, id);
					ResultSet rs=prs.executeQuery();
					if(!rs.next())ps.executeUpdate();
					i++;
				} else {
					throw new InvalidIdException();
				}
			}

		} catch (FileNotFoundException ex) {
			System.out.println("Problem in line number - " + i);
			System.out.println(ex.getMessage());
		} 

		return list;
	}

	public boolean validate(String packageId) {
		String regex = "[0-9]{3}[/]{1}[A-Z]{3}";
		return packageId.matches(regex);
	}

	public List<TourPackage> findPackagesWithMinimumNumberOfDays() {

		List<TourPackage> list = new ArrayList();
		try {
			Connection connection = database.establishConnection();

			PreparedStatement ps = connection.prepareStatement("select * from Package_Details where no_of_days=(select MIN(no_of_days) from Package_Details)");
            
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				String id = rs.getString("pk_id");
				String src = rs.getString("src_place");
				String dstn = rs.getString("destn_place");
				int days = rs.getInt("no_of_days");
				double cost = rs.getDouble("pkg_cost");

				TourPackage v1 = new TourPackage(id, src, dstn, days, 0);
				v1.package_cost = cost;
				list.add(v1);

			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return list;

	}

}
