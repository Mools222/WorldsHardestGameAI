import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Test2 {
    public static void main(String[] args) {
//        double test = 24.2;
//        for (int i = 0; i < 500; i++) {
//            double random = test * Math.random();
//            System.out.println(random);
//        }
//
//        System.out.println(1 % 9090.9090);

//        System.out.println(0 % 0);

//        double[] numbers = {0, 2, 3, 4, 5};
        double[] numbers = {0.8824948572219534, 0.3306564375931146, 0.8736615761119729, 0.9769722396849304, 0.320347441429948, 0.5624571218201248, 0.9512832431819481, 0.15826973534211153, 0.22486047317716962, 0.0595940804860019, 0.10096315611545159, 0.0862082541246747, 0.5640818940008673, 0.10179173217499682, 0.1703987361495244, 0.06115462692340945, 0.07995199874468084, 0.39794600429072113, 0.36275227227208573, 0.1290857955552518, 0.27368905356833306, 0.440701065589044, 0.34097944216417053, 0.5902427786414065, 0.35691527098127795, 0.6192749603161852, 0.9100894994898873, 0.06482067121470847, 0.3882925632620101, 0.08708389324270605, 0.6549277255859678, 0.2078827969008118, 0.3186100863078013, 0.12314131070846313, 0.3362260700945753, 0.6684350707989006, 0.12024604772984226, 0.01410926564529591, 0.3497571517529715, 0.08242913721967227, 0.6129824819139073, 0.02333515403658437, 0.01767597767045781, 0.30608105201788405, 0.9803715036147014, 0.07034916383111633, 0.3007007485557516, 0.8884216984489183, 0.2993557929574705, 0.08104604618214362, 0.10897833685078928, 0.809696236214845, 0.9024763542459124, 0.11715617705470893, 0.34903437478700694, 0.28610269845525227, 0.29741760657530725, 0.40107687544242754, 0.048737263711311374, 0.032557359897839144, 0.6039291503198213, 0.3111368077356035, 0.1761445651310457, 0.23120757071369358, 0.628473750605214, 0.15356269639813847, 0.20210429679892628, 0.4441463488942341, 0.8992418274267714, 0.39583485889197056, 0.2770245037710082, 0.6842756824898967, 0.9503427827299158, 0.644093197195687, 0.4026067509160237, 0.4119721378724076, 0.2615262706669458, 0.4115998321585509, 0.16468750269200327, 0.4870548273520702, 0.3748642075015196, 0.30250120822901383, 0.4128264126608078, 0.44487509185344354, 0.7597140633796967, 0.042425831596511254, 0.15197613723929337, 0.1165628074498627, 0.4323500479099456, 0.1314388752171426, 0.6341324904559047, 0.49466693955706376, 0.5181739467038332, 0.243913960884787, 0.32750358128612034, 0.15986091536894953, 0.676256567846386, 0.6462288925430537, 0.3742003719876128, 0.9789367075011841, 0.36215103284682704, 0.527541365713537, 0.8845029102366446, 0.14424165469586991, 0.439474125341647, 0.44843536747580715, 0.45115218812020175, 0.030387534319163967, 0.1316003865167672, 0.2047855972403323, 0.2030650326402974, 0.3220091557607033, 0.9015056450011798, 0.0902153368307772, 0.0934073858956408, 0.3026623109274835, 0.1732542158075714, 0.2537259537914882, 0.23717668693263017, 0.4866380132898277, 0.8350237947598401, 0.29166731792607237, 0.8767725727492552, 0.585826156820062, 0.10191951883002515, 0.5263666989247997, 0.457529136056278, 0.35218557569715225, 0.17523252760334707, 0.3464444088207632, 0.07570826458217339, 0.5159303592769434, 0.43804135223073415, 0.7474599047403457, 0.38102308284682085, 0.4951233499965575, 0.10770915941248393, 0.33489734301842766, 0.09372479349058316, 0.5131653658814413, 0.029316043131988434, 0.5667195594488751, 0.718744235300542, 0.5455179239224515, 0.13937591623393375, 0.270013388707642, 0.47796031790392723, 0.30413307251706667, 0.3921173076570491, 0.11183007722241001, 0.23976683372689078, 0.1539760674933377, 0.9119060149113909, 0.12292119154338366, 0.27346486741333564, 0.975643880845417, 0.44454936147249635, 0.4912826448596621, 0.8018742392546359, 0.6419221686203037, 0.31221534463466594, 0.25257794246794973, 0.35112933184965234, 0.8504819882965177, 0.3931190814015858, 0.06990671883825161, 0.3978285707445054, 0.019910300015054117, 0.039078328921659966, 0.2807014496296869, 0.27018245969745025, 0.3698654186093465, 0.22606929586983515, 0.07229305045008683, 0.31573344549326543, 0.980524774109134, 0.8427283227991673, 0.8565088756301018, 0.11936230414799998, 0.2728510936363646, 0.33064655171046253, 0.490424615548695, 0.13216186257222695, 0.12158414812029594, 0.7787747247010228, 0.4439732954313703, 0.5455444750927576, 0.2278948526808363, 0.26756254467723106, 0.3334188554418821, 0.8364848103575456, 0.6307262005072275, 0.41634238737096596, 0.3446458467706658, 0.21644850527118464, 0.043405138332790694, 0.43577277141957027, 0.37877226836139855, 0.30686783845186116, 0.5800451010791593, 0.518534988548773, 0.36310854980252827, 0.3178424137183784, 0.19870836241138679, 0.48126051753015053, 0.4636662321517091, 0.08738679961071805, 0.6809539647224713, 0.599705948309529, 0.09447042671223826, 0.5164302143214042, 0.17959342010338275, 0.5229991743985221, 0.5302323717847366, 0.3306992837596133, 0.3520699660796842, 0.8480547251532348, 0.9580489339704231, 0.9906372122476569, 0.4577764050595716, 0.11958734583213437, 0.45414972368419215, 0.3461341305106528, 0.12456554328900526, 0.3921843681355639, 0.18847759874740044, 0.01756579530496738, 0.23371819479767109, 0.5505102560465648, 0.47239563518978656, 0.2156390214680487, 0.22217130615417247, 0.4766939756642815, 0.5171351736092747, 0.1784709256759004, 0.20168228541818878, 0.6032687169705178, 0.8615586924012252, 0.7994874323785658, 0.07269551395718099};

        saveWinner(numbers);


    }

    private static void saveWinner(double[] directions) {
        File file = new File("AI2 winners list.dat");

        ArrayList<double[]> winnerList = new ArrayList<>();

        if (file.exists()) {
            try (ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                winnerList = (ArrayList<double[]>) input.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (double[] dir : winnerList) {
                System.out.println(Arrays.toString(dir));
            }
        } else
            System.out.println("Creating new file for winners.");

        winnerList.add(directions);

        try (ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            output.writeObject(winnerList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
