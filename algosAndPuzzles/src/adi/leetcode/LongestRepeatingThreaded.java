package adi.leetcode;

import java.util.ArrayList;


public class LongestRepeatingThreaded {
    


    CharList[] letters = null;
    int max = 0;
    static boolean debug = false;
    
    public static String debugString = "COPAOAORSHCEDQGHBAGJFBETQNRHPQMNIOMCABKGHBDLDCJTJPKFBQIPJDONRJKAIGFTDDFNQLIKRIJGKQQENBCJTIRIQFKGMJNB";

    public static void main(String[] args) {
        String[] test = {
                "GGUIVILPZJXWWZLVKETZWWFKDYCRQICLYYJDMRCQVCSFCSKRMKCFUNAECJVRLAIWFJADSNNYDPXVYGPNCLLCQJBAUVPTJCWEXATZVJVCPQWLZNUEIAUHYCBMFJSJJHPBNBFLKSGXQVQUBYFORKGFPPMIJNMSTBPPUBNYLKFAWDHTWEJKHNLOCTJQUKOPSGOLVYUQVDKZNEHPLRVHNEXGRPFQXSUUPWASPCNMPLQLPTUWNFANSVUEOBXPTRRJEEVIXEFUYRMTITPDNFYUVEMXZGMBTOLRWKHQQINSWGKVBDKUGQZSHFKPLQMVHUWMAVZGQJLTFHONMUSQUHZTQCKARLOXZETTQQNMKRNHIKOBUCMRGFSPZOIXTCLEKJOMMERVPKBLKATXOAUCAJNNFNZGROGOYCWUPWFSJXDYRIMLQZRQQMEISASTXVZQJRLGIKGSAEPGJAIHKFJFVNPAGQVTWIHKWQTWIGSSDNAPSKXWSOSCKZDSELZYQJKABUDCJIZGNBBAEVFMKXOQLZLJSODTUJJRUGEAISYVSZIHNYFDULBMEDDRKXTFPTJWYIDEKUSVNFIVCOYCHKJNSKEJNAHVZVVXKBSOYNXZVKKBJYBSNJXYTIDMJFWVGFVOVRDZSMKCIDZLAHEDXNBCZPJPZJNMEVYIMRQNPXVMEYCSJWGTIGHGTCERQUCMBVRWPGMFEMWBUYCXBKNERVVRKOFCJEZUWUGYPKTVBTRBIOTZUZYSDNVJAFDRNVYKEWLONXAXDSVRHXSMVPAJSXJNZFFQWMPEMHVVAYWIXHSFXZIZTCRCLDEPRWADXSDKTFVNWJWNIOERYVJDWRJXCJSSCTDJOFHXVSNHXZCXQNGXDWXSEPRMROPGDJYGBCDPFZHSJLMHFREFYDXEGKGDVILPDLYTDAPGUYGGLAECDMTCATVRNQUUKNTCYFBADMPGZJAIXNAMCPCCAKWKMRGKRUEQFXJRWHCXYXZPYLNGBSAAGBALUUUDMTSIBAIDVLBGWAPTPDGVUZHUXXYPLYWWBAWNYOQCDOYDIRFOFZOXQTQAQUCOHUDMBVROXBDXZQGTMUJSMLGPBEJCEYTADSVVSIEKIOBYHZEODBSAIYZASOKFOVVILHFRDHRRFCTOBUSQXKVSGCAKZPBILKAIVEJDNKAVEMLCVLOMGPKPHOAVSOFQBAFCMXJJJLMUVWOYBDMGBHZMUNEKEUWDCJAJRWNZQMSVRILFAQCUAQKDGENWPFBFSCETWDAQDNVLORCDEQENYPOILDWPIPINNEJFOTFIXNLJYSZMEBBHMSOIHARKXZLEQVHKSNEFVBPDYTCXWUEKIZLRVTHAUNYAPLSDIMHVTZIPCSKWHGMFHBYSTNZSWIRMBDRWPDINVIYTEELJHBNDBVAZENFHORYSUPJAOEKIHXSOVLZZCNEFXAHQMTAHMLNNHWBXKRWWGZJTDTPSEMVVKGAWXSCJBOAWTNGIQRGODWOFCIQYCJANKCCJOELIIDQVRUIHUIQLWZSYHRENHXQGYKXRINPTBXGEUNYIBZPMENWVSVOFNNBLJHJUDDJTDQGXNABPCAAAINPFRIFNFGVQZURJMFGCCYIFNJYFVZQZIELQUUSQTAAOOOIGBLPVOIQPLIZLQVXZGSWGQZUXQDRIBRFDGXRKAFYJWMUOANHKOBRMQVHVAEIVCRNQOAWPVBRRPZJQZSDLAANATNGWLTJALQFREICUIXTFSTJAYHQVJBPHAJOMTPBWAGANDOSGIKFJIAFEGRSPWXCXPPARWSRNRZIOFOMVNDLNNGCBVOOSPTFQQCNYDFXIZUBJUHHFZTYVKOXVDDRVICTXGCOZLBCKEHYAASGMWIBVZFIXOSQLUFXFMQDABVTAXJQILPYHTJQOSEANXYTVRYZWENFXTVPCZTXPOIYCYAOLQVYDBRMMNFTYKKMFXAISADZNIATGJAUUFREZGOGDJGGVYFKYCTORORXZFETRKLMSSZBNPLXKHPTDWAPIRJGKSMWHWDVYEPCFTMWXEFNPQQRASGBEQZOLLIYHVQDQWRBQWSHIKOBMXMLFOIPHAVILRXERYACNFYQIEIFNOEHLSQDTDBTAWDRJHMCTMDZLPEUJADTRTYGTFMNXXEYLNEDIDCUVACLDIOCOAYXPTYATDMXRZPQDBHKVQGINNHKALPPVPDZBPMRKCBZEPTMWSYWQDGWYKAZPJORYLENUDLZBFWOJNVLCKJYJWRXGWWURULTXOKKCISJBHRMVCELQSOROWBYIGJUHSMMEMCROQCNEBGIPZTTOVDUXYXSFPKBVNEHBMRKQNKUSXUSGJSTGJCDXZMVKGWJIRAKCUOYUEOKFRBJUMGWMUUTODTDMRCESDZHNJBAPZVGYBLBDXKXAXIRPMWVOPVEVXLIGRYMPMREHLWRDIAYWDYQLDUNYUMYWFSAIIBDIUNRGPGKUSBAGHRUTGIVTPTFEGOSUPNQLKETROUEAIEWEJBNCDBUGJSEGUSCXPBNJHLOHAALNCMSVKVUGCUWXPHEKAYKDJUYKBXMWIJRFQHOIFFVUIGMUOZACQAVBXZBWRLJJNKJSNHGHETXMOPDRRWWXNPWXYADMOCNCUFSMXDEGPTHLZZOYBCOAEEVUFJXFOUYVQWBPCSHIEBZWAZUUPAIXTTEREKYYTHNQUATTDNQFBBJIQDNAEXYPKRUSAEEVVCDAAXSMNXXDSPLWFVCZVUPKCFVNYMGKYRVZFBDSLORQXWIUEYOFDOVWOPLZKFWQYOIYAEZRQZLQKIGFCGXDSKKSIKHBYDMWZUPBBDUPUCVYWHZIEBYFQMJUWHPIOKWSMOSMXPBLFXVVLXMYKXKAEHCXLQWIYULQTNVIUTXTOKZROOXLULLCBSUEQKXVGBXHZDCZLKWTOXNEWKKMLFUWQADDPCGTHCAOQAHXKLGXUKJDPTYZTASGQAFOYHJMIDXMOWWJFUAPBXBGJEZJXWTWWKWHRXAELXYJXFRTJJHOZWVDCETVPVQKAVYPMYNGRMIKKUYQCHHAKVQJVNRQRMNOQYFXGPCAACCRBYSWZLDVEMTEZNOESIDWOJQVQGHHBDEUMNFKSZFMBMTAMLFRZWOCJTWGSOWDUWBJSBEHOVGMMNSZNBFGOMNAGGPLGKWDDALFMXCOHGHWKAGDOHBXVGRMRIJCRQFTMHAJEJKGIUDIKFGWGQSGKJBGOWZUVSJYYFCPAEJWNLWTGVQQPUYYEFFOZWXGWGSRYDXPUZIVKHTTULABWWAKISZGTGSNUITQMTLJXOYCYBFPAVOUAMJLUCMRAGXTJQKXQFJIMOMXDXWQNJOPWSPWXZMJVCSRNLZYAOOVDUVMOOIGKMRVADDRTQWTLMLPAFWSONDTIRPUAUAMPJUAZRLIHLGLUKOHXTLLDSGCQBZEWVHTWTINAVDNULUAGNNGIVWDOMPHCDTPDVGJLYLSUMQQSVPHNAETDGGIOQPPOILSMDGTOQZBZZLEHNOZHZCEFITTMEEVGHFKGVUVRMSNEJNVTLVCRKIMKJBZHBJPJEDXBOFRLSAMCNEXPAFXVRUORPIFGAMWQPLTBGQLUBYIHEPWSLPEGHAOEZFESFBCZQQMYRMFUCDZXPYYVOLGMHQZCCYPDGCQLIMJYHXWATYEVPOHUJDHJHZILPBOGJVBYUYHTOCVVECOBZPWCLVCVMAUOGIKSEBLWYZQYCNPECVOKFSBIYTPHZTVPQTSDZXULAGZLGGHSSQOOZFNOFBPTENBHVURGFBKXRGGUKFHGZIIEOAXMCGHJAZQMNRBHBOZWDXLAVMPFTRXSGHOHSEPRNNTAVYWSGNUUZFTOFYWBVEQVKSBUXEUKPFICJDPLPAEVMKPNNTPAREICRASNVGLGFYYKRPCMMHPDOIZFIEFUGPUWTBUMFNHCEJJQQOKVGNDWDHUOBYIYFIOFTRDHFIJDCXAWPZJZWXRFFVXSYGBSTFRFISCKIPDDXQLMROTOWMONTULAGYTOIIPPWFTCZSKXPPKJGTTBTJHYFEHEDECISNZLRIGDUZPTAYSKDYEVVJGCTMRKUXVTHOIZZJAEULSVSHGXLGGVCZVTEBJIMFNVXEXHBQPJILRPBCXCLXHDMWAHSYUIIWUXHNGLIILQEXDPJKDRRTLNPFAKORFUNIUQUNJBLZIXQRMUQBBWUSLUDDDVPKAMNUPAGTSITEFWEHKYKANXHLKHHNBAXVRUNLFYBYNEFUVWGRFBIFIAJURNAYYXKGOBSRZRWCIWHUHQJYMKEGLGCVRWHRRPFUKNNKRAEIIDSMUYXYKIMUOERIOVMLDUQIFGHENAQFHEPGKBKGTQGYOKZJDDXANCWFKRSRWAKUNKKLGXTMUANEUSAELWDBRBLEZRMWBYONUQAHFBMWNDKCIXHTVMWKVLTCPZJTTJNIVDOSTOXHRGOQULDXYEWBECOGSQHVVPLBTYXEFGWCDHFORQPLZVZPASYCDDDBYSERCZSRUGJKSPDEDQPPWVXHXYRHBVWFQTQEADIKBOSZIFKWAEQQLEFLFUTRVNUWPZPPRBXUUHBVADNSJPUPIKVORQQSOYVLEHUDCJUVDMTQMRAKXDMIIDGTUAHHLCEJCTRGZCAHIKCTFLZRTDMTFWQGIIVSOXPBQEQTOTQEMTMDNQCAONKUFQGHGYYANICAWWSLZJIICHYPLZOGCKOPBLJKVJTLOGTPYQPYDVBBYECBUAFETGUOMGHYPMSNPDYQWVBMYGAKCPWLLIYXPOFRCFQDLWYMZFOCALQOBQWLTKFPGKFXXGVPJOUFQOPBENFBJTZQRAHGPQAEUCVKCVFQSXZFYZFMSIRIXGCHBRXTVRCYMHBLMQRXCBPOZHIBUDIVNUQCHQHHLJUKKFTYJCKGIUXNDTQHAJSLJQBJCVAQGYAOZZOOUGFZYBCECSHQOKYGTTIKSYLCCFAIAYLHEDIQSSBGMYHSMQFZNGSDBNFORIXTKHRQVBIOHVLCEZCNRLBVJAJVUWAMJUMERWLDEZBABJQZSGROERFZBUSSLOVPUPTTVHVOALEFSBPJILYTIJKCVPEFVOJHLSRBYPAQAICBVEAXHDRRJPMFSBVVZSIDSTTWQURBPAILAKFVZMCMFFZNMHDFDOGOMOKAYNKYWMXWISGSZWVTABHXZGHCLBAGCPECGXLMOCGDUXATITPQJJNOGNTQVFLOLNBLQUSVWXDNWLSWPQMVQAXQYTEJAOJGXNVQRUHCRVTZFBQTQIRUFAMFMZNZOLBZSPOOFCQNAQGMIKPNUVVNHPHHVQWKUMWMIICPYDXMQHKPAICYBBXGJYACQMJSJNJQTLCKKTXLKOQGRVXMMXAJWORYLTEHAQUOAHHWTXIFXIYNHKILIZKLGGTOSDVDPXGVRSBAEQLNHFIKPSADHXUJZDWXYGPCHFHZPQRNFBOFVTFJGEWKFKGIMQUPQINJHOGNARVQCCFMCIQLYCUGVEYBLOAZTCOFKQKEWWQIUVEJUMNWFTLNZNJDNTXUGRZLBPYCEXMKGQNUEARXQHNSKEOAXGKPNCKNRUHORVHBYQOOKRGBSLYDVGCYZTSGJCNMZQGUBSFNQHDNWLUGWLSEGUTBGJVYMYLOOVRLJVZDLJSVFCFAMWYFWFYOKLYJCTSYATNYLQXONMFHTDCHHFUOIJRUMBJCLIPLLTRYQUVQMGWEUZQYVDMPJAENUONSUNXLUSOBJIYPWTDPUWKCGJFWPOUKIYUUXORYDGKNSGPBSPTAHHJUZCNOITUFNLCLCJFTYGKXQHAPTHTTBTJWBZDRBJTGUTCBJMIYVETCBZAHNVOILBJKYJYYCVKWWGQFLIKPECOKSEEMBDWRABXMGVBZQWFYCBFBUMKNALEOBDBKXOSQRQENKFFJHKUSKQPAPQEBKOLICBGOWRYTXQVMWODJBKMCJMRIIJIQLQLQZLQBBGVOYEVMZBDNTQWNWSMUNCOCYBJIMRNQZHQUEGHAKGPSUFUGPIKLHDOZRCKZMLSPRWHXGUCFPTRLGIZZXVZWXBQHBHMRQWVUHPFREKFUZCUBEULVZLRZYVKLNXLYHDRMIHDDSINVAOHTTPUJKLVNAAYJSPGODSNSANFITXTOZANRBLHQAEGRPDKZOBVXXEZYKMGHIHHESBMMHQEEOOMCLRMTLHROLPBMKEVGMXXJADOFTIOBTRKZEMIEALBHWHMPTATKODYHTFULFCIBRDTZWZUFGWNGTCFZCYISAXPUUSNCPFMEFIPYVWRXJUBLIJSFHAQRNNHKXMDDVLUQHWCSKKKJFBEHKLMJZDEPRZSMYFQMIBJKDYIZDUFVXCMYHVTIHTSZCVVGDKWDQEYBQTPAOKPPGGNYWPRIQOUZITSPBAHZHTRNLPNCSHPFUWJJQDVMZGEXZIKMQXHOQRCIEWQLUQVFLELOUFYMDJFVCJWJIVULVKAZBHRPSCNVXEPJHINOWPFMEUSGQVMXDBHPEDAOXWBBXGUOHVINJAVTVXDVPVYRDOFENTCHZLMIOECLJRXMFJAOFHAZNZAPVQZEMMTJFDJIHURPBCCDXHWNTMSGYQQGLOGRPVGWWJFTSKSUZAKKTIEXNEGVADHGIGSJQAFSRNMDVLIZVLIPJQJZRMQAGITGCLMRALDUMVRUIRZRIONCEQDAWEOXMCXGOTKYPBICDCRJLDEJDWREIGWVVOKYOCNOQHGWNDELTIBCNSZLHZAPRWBRMHJQKCZSTNNJUQIIFFVPXPRXKUQVNDEQYOEITDYFZDQRBNCUEMQEYLNHHELQSWXWCDURSEQBUWKRYDXBGUQMENGNLGANERVIGDSMCJSXGUGJYZSRDLBUWGMBBHWEBMMDIZLRACVKTTJTJYNCQHFEBIGTWXRXQMUKCHJQNKZRARDXHSOSLXYRXDCPKMUWUGMFGROFHQPAYTABBZEFUAVVIXYJGBJIXIPUBTAYWYIKRCNEMIVOWFBNLTBRRLNRXTQEHGHWKYRJYIKFTSKAHJQLVBSUUPGTBUVEARNFXIVULOTNOCFDIMPFORADOFOBPKIFXRZZCANBXNTVCCGOLKDUSSPWIFVHMQJCPGHAMOHBEDAXIILTXEDYBUNSMYYITUCIVWJFXJPZLZZVUZIOWUQQUZEEGODVQFMDKDPQPPSQTVYVEYSAIPTCINKVWKVZIZPSZDOCFDHHBVWLNDZZLQPSNXKJBERLORWRKIQHOOXOKBODPALHOVMDIACIBSUHWPIAURJDMVKPNMFCTSVRKNFBTARMTDCDZKGNHTUTMTJFICCCSABITMGWRMUCHLQMBXHUGZLAKQCEYZUYRDYLLCHPGVLJIRPTRHYSMUYKEVKOPKMDTDLWPPEIWJLLAGDIOTKOUXOVEGTHDCCUZDYLRHBNFJVAXSKRIRCZQQOYXGEJECKZDOMWNVLHKYBDKRMZANLUHMEWOTEAKCALXPXUBHDUQEXQFQSSJEQUBHVCHRHHJNRHLYJMQJCZMIANZMAKJPHOTMTSGRYWUJGQNYYSYCDOOYGVMGFCVKMJNLKAXHCZIOSZDKBZOTQTPESFKICVKHSRTNYNDVPFJOVBWALBDPQIJRYKECTTVEUOXVGBFYYLBZDITKPQIOSOJRXMPMVTDXFMIJOOPSTYKZFWRLJORWYKEXDNUTRGHTTQJDEONAZEIAJURXVRFZJHCIYOJCRQMUTLOMGWMYNZNIPNYMCUXCRIPJNTKXMZRWQVNTWWOZTPYUNLRXPBNUAUAXOQEMXJMVRUNOPZMGDFJUYRHXTLKZPIAEWWJYPSCKKQTKYNXCISUAWCQPBZPLJJINVKFNQTPLZCWIFCFUDNTWBDHDKNEBMMDERFLZIZPUYIQCFQPEWZUFMHSKPUHBKSNWCYHGKCTRYBRACSLHGVHTISIJBIDSEGXYQATMJVPDUSUVELXGSMCCOSADIKAHNXYARKGQRYEJNMRQARBBTPOSOMIDKIUWTMSIKHLDMZNTPEHKFSZGQKMRMLZXADOSHNKOSGGARFBCAZVQUXILUCGZKCIRDLEJACGZEGXOVVNIECICBAGHZTXDXMHSHSBTXCTBOQOQHTXLLLJEMWRHEIWBOPSUVXZDJOZRPZLVXVOEBXXTUXJANHZJXCFKGYEDWRTQTZMVIGNXRRXIKFFTJRVQUTALBRSSHSXDBAXCTRXPXNESIKFKMZFRBVXBZUOBBWAWPOOFNVBLOJDUAQCTMVLIVTXMKQSTRTUQXTJIARGNUIDDVLKBJACZOPSUFDBJFZSQBDYVOZPFTXDMSMRTZKYJUNYCFKZKHOOBEYHFLTIEKXCRLNZYTOVZKBFMCSDAWEBXRLTAVOAWKSSGFXZHNCOUIPXFIVKNEACZJUHCHUYZJZIVAQVXDYDLZMLPGFIQEDITULCELTSKSJQKWGJUSQGIYUNTXMZHJQDRZHTVDOTXFBVVJRJPFYDGCPQZTYIWQMULIPPXNACUTOKJYBULGOEXNBFUQAEBGJVDHJIHBOYJFLFMAGPBJADKHSNXNHJSGFPBXPHGQFTGEKOBPMGTKYJLZWJUYXLGAMFCFWREPCMWLHSVMAGUSXTNUGSCIYQAWVHHIJQNQXTBSYNEDRDJRVJQFMCIJIXVJKNSZBULKCATAIZBXKLUWCHCKEUAKLDFBWADPMYPOXCDKTVCYEPCQGSEIBAKELDCWHHMSRHKWLOVZQDADNWIQYFNYOMJHCIUGRKYDFUTILBUCZGKJKQZMXDVGCZDKFXYMISBNMKSRUFARSUMDSRENQRETHWIJDYOSULKVDVAKDXSKGNSHGRSJDGNFXSOBMXTOEGIHQKWLEMSUJAKDGUOLYIYCVZXFEHWFUDLMCGZYDUAJWOEUQPWVHDFRZAGBFVGOTIMGXGMRVSPVIOASOWGADBTCWAUYAEOVLKMJBBZRITWYKQJYEXOGQLZDCNJTFYRBGZUVYGGUYMJGIDQWNOQNZAIRSLLGRODNOGIPIZUZOELXTZZLLUHNPSXKLWOUFYNTTUFMBMGWFBIURNSAXMOPFTWDSMANZSNESOYQVLZCOZSGRSRYOBSWVJBPIQWFXTEIRBRJXXXBYWIYWPQXOVNSZHZLPXAKXSNJEWDKRHPMEBGKDPZKGSCBIMOSSIAIOTCYLBDTWBIOJTOJYDEDDPJBNQLGGVWXIJGASJYNWPSMBAHWLIYSVWWATJMGLXTORJUOGUTZJCLSIKGUWTAJQLUODMRDSOXTYWENOPFAYJLJCJYWCKARVWGMMMZEREZZRZRIYRUVVICJCIWFOBSHBPMIIUHVZVWPMHPKZBQLWTAZETNVFVVEGMBNFKNOOYHVHNNWJNDOMGVPVYRCWVWNWXXQUAEZZVQTDYNYTSAJCLVGDZSQHNRDBPVZUVOUFILTIMAZXHAKWZAIDETWXQXKFOOFAOUWSPSFFNOOMXWIOHLOUTMUWHNARDGPTHNQIDSGLXAHTWSUMTUHLIIWOHZPWTEHJBOEBUFDODRJCHVYZGLTEIYZXDZVTHGOZRXHKFPYHIZOGQWBUGTJYGYNNKERORDYLRPRRUFDRIEYZFSNVGICYQURXDCCNBZUIARXWUELWQPCKERYMKEAZPMPSJFODLFEGCVEZXEHFTJJCFHUXZWTJIONJPAXARJXBUTJXNJDYAAONEMLMKFTZNDFCZXNNZPANZXFSFDEADRLOBXYHOCWBNVEIYEEGXUFAJDUVIERTSSOCYXMKUBUOBNJLXQDXYAKGEVENKYOMKJIPXKJRVINATCVYEZQFMQTGMKVGFGALSMCCHFIIWDMAWZGHOZXKOAQQFGQQFKWPGVYISCBOTFJVUEAMEYEPSPMXRCWCKFYUUXHHDQSUFCZPZFTJAMKPKFYHHSQJGKIEGINKJLDRQXCRNRXNISLXAVNWRKOVBUNNYQRVBGKCISIIRULWMXEUWXRRJNMZAZJYEIKLUVGNXRKNRKTBJRCLAKXPOCMUEBUYDNLMFHEUHBADMIWYFCEXCFTLYJRXPLWYSHIOLPFKHBWJXIZTMBHFOBZBYSFESVTSBERKIJISNFKYNBLDYTFLSHGVKYJSOWOTVNCXV"
                , // k=7520, 7864
                "QLHOSLDHOOBHFLPBSLHMSHMSRDOIFGGRTTSMKKRIENQNEECPLTJKCDMLRNNEPQAJDQFPEOGLKRBHSOMHONNTKLFHKNCHQLDBACMO", // k=7, 10
                "COPAOAORSHCEDQGHBAGJFBETQNRHPQMNIOMCABKGHBDLDCJTJPKFBQIPJDONRJKAIGFTDDFNQLIKRIJGKQQENBCJTIRIQFKGMJNB", // k=9, 12             
                "BAAAB", // k=2, 5
                "AABABBA", //k=1, 4
                "", //k=0, 0
                "A", //k=0,1
                "A", // k=1, 1
                "AABBB", // 5, k=3
                "ACDBB", // 5, k=3
                "ACBBB", // 5, k=3
                "BBBAA", // 5, k=3
                "ABCDEFGG", // 5, k =3
                "ABCDEFGGH", // 5, k =3
                "ABCDE", // 1, k=0
                "ABCDE", // 2, k=1
                "ABCDE", // 5, k=4
                "ABCDE", // 5,  k=5;
                "EEVVVEEVVVEEVVVEEFAABECCEEEDDEFEFEFEFEFEEGEEEEHH", // 18 (E), k=7
                "ABAB", // 4, k=2
                "AABABBA" // 4, k=1
        };
        int[] k = { 7520, 7, 9, 2, 1, 0, 0, 1, 3, 3, 3, 3, 3, 3, 0, 1, 4, 5, 7, 2, 1 };
        int[] expected = { 7864, 10, 12, 5, 4, 0, 1, 1, 5, 5, 5, 5, 5, 5, 1, 2, 5, 5, 18, 4, 4 };

        String[] test2 = { "EEVVVEEVVVEEVVVEEFAABECCEEEDDEFEFEFEFEFEEGEEEEHH" // 18, k=7      

        };

        int[] actual = new int[test.length]; 
        LongestRepeatingThreaded obj = new LongestRepeatingThreaded();
       
        if (test.length != k.length || test.length != expected.length) {
            System.out.println("invalid test input");
            return;
        }
        for (int i = 0; i < test.length; i++) {
            try {
                actual[i] = obj.characterReplacement(test[i], k[i]);
            } catch (Exception e) {
                System.out.println(" at test input index:" + i + " exception:"
                        + e);
            }
            if (actual[i] != expected[i]) {
                System.out.println("failed at index:" + i + " expected:"
                        + expected[i] + " actual:" + actual[i]);
            } else {
                System.out.println("passed for input index:" + i + " expected:"
                        + expected[i] + " actual:" + actual[i]);
            }
        }

    }

    public int characterReplacement(String s, int k) {
        letters = new CharList[26];
        if (s == null || s.length() == 0) {
            return 0;
        }
        if (k >= s.length()) {
            return s.length();
        }
        if (s.length() < 2) {
            return s.length();
        }
        max = k;
        char[] sArr = s.toCharArray();
        
        boolean multi = sArr.length > 100 ? true: false;
        char prev = sArr[0];

        int start = 0;
        for (int i = 1; i < sArr.length; i++) {

            if (sArr[i] != prev) {
                addNewSeq(k, sArr, start, i);
                start = i;
            }
            prev = sArr[i];
        }

        addNewSeq(k, sArr, start, sArr.length);

        for (CharList list : letters) {
            if (list != null) {
                int listMax = list.closeCurrSegment(sArr.length - 1);
                if (listMax > max) {
                   // System.out.println("Max list of length:" +  listMax + " for char:" + list.toString(sArr));
                    max = listMax;
                }
            }
        }

        return max;

    }

    private void addNewSeq(int k, char[] sArr, int start, int i) {
        int idx = (int) sArr[i - 1] - 65;
        boolean listCreated = false;
        CharList list = letters[idx];
        if (list == null) {
            list = new CharList(k);
            letters[idx] = list;
            /* very first occurence of this alphabet. */
            listCreated = true;
        }
        list.addSeq(start, i - 1, listCreated);
    }

    /* Each alphabet has an associated charList object.
     * This list maintains <start,end> pair for occurrences
     * of the particular character in the string.
     * Each of these elements are called as sequence element.
     * 
     * For eg EFegFaAbbccAAAbCcBDd
     * will have:
     * a -> <5,6> , <11,13>
     * b -> <7,8>, <14,14>, <17,17>
     * and so on.
     * 
     * The processing model views a charlist as:
     * alternating <gap, seqElement> tuple.
     * 
     * When a sequence element is encountered, the gap
     * is calculated using prevEnd value.
     * 
     * The sequence element is not added right away to
     * charList. Rather it first checks if the gap when
     * added to prevTotalGap crosses K or not.
     * 
     * So let's say this are the occurences of an alphabet.
     * g0  l0 g1 l1 ..... gi  li     gn  ln  gn+1
     * 
     * where l represents the length of the added sequence element
     * and gi represents the gap between the lastSequence element
     * and this ith seq element.
     * 
     * GapI > K case:
     * Now, a gap can be greater than K, in which case, there is
     * a barrier and the current sequence segment can not be
     * connected to the next one. In this case,
     * a new sequence segment is created.
     *    createNewSeqSegment:
     *      - if (charList.size() > 0)
     *        closePrevSeqSegment:
     *          - processPrevMax taking startGap as the gap before the
     *      sequence segment and GapI as one after the sequence segment.
     *      
     *    - start the new segment.
     *          - set startGap as GapI.
     *          - set prevTotalGap to 0.
     *          - clear the charList of all elements.
     *          - add ith sequence element to charList as the first element.
     *          - set PrevEnd as endIdx of this seq element.
     * 
     * GapI < K BUT totalPrevGap + GapI > K case :
     * This gap is eligible and hence the ith seq element is eligible to be
     * part of the current sequence segment provided the current sequence
     * segment can be truncated from the head such that totalPrevGap + GapI < K.
     * 
     * Do not yet add this ith element. 
     *    headTruncateSegment:
     *        - find the point till which list needs to be truncated.
     *        - reduce from prevTotalGap the gaps in the truncated segment.
     *        - set startGap as the gap before the first non-truncated element.
     *    add this ith seq element.
     *    set prevEnd.
     *        
     * GapI < K and totalPrevGap + GapI < K case:
     *   - Add this sequence element.
     *   - set prevEnd
     * 
     *         
     *   
     * 
     * 
     * invariants maintained:
     * for every ith sequence element added:
     *  prevTotalGap is sum of all gaps between 0th seq element to ith seq element,
     *  in the charList.
     *  Where 0 to ith segment is part of a running Max.
     *  
     *
     *  prevEnd is the endIdx of the ith sequence segment.
     *  that is, it represents the endIdx of the charList.initialized at -1.
     *  ith gap is calculated as startIdx of ith seq segment - prevEnd -1.
     *  
     *  ith gap can be added to prevTotalGap which is cumulative only if
     *  it does not cause prevTotalGap to cross K. What This means further max is possible.
     *  
     *  When string ends - close out the currently running sequence segment.
     *  That means add max of (startGap + endGap, K) to prevMax 
     * 
     * 
     */
    static class CharList {

        int K = 0;
        ArrayList<int[]> charList = new ArrayList<int[]>();
        int prevMax = K; /* When the gap sequence crosses K, prevMax is saved*/
        int prevTotalGap = 0; /* There is only one sequence segment of ongoing gaps.
                             This is the total internal gap between sequences. */
        int prevEnd = -1; /* The index in string where this character sequence
                             ended last time. Used to calculate gap. */
        int startGap = 0; /* The gap before the new sequence segment.
                             prevMax uses this startGap and the gap left
                             at the end of the list. */


        public CharList(int K) {
            this.K = K;
        }
        
        private void startNewSegment(int startGap, int[] sePair) {
            this.startGap = startGap;
            prevTotalGap = 0;
            charList.clear();
            addElement(sePair, 0);
        }

        /* String may end with a gap.
         * This is called at the string close event. */
        public int closeCurrSegment(int endIdx) {
            assert endIdx > 0;
            int rightGap = endIdx - prevEnd;
            processPrevMax(startGap, rightGap, prevTotalGap);
            if(debug)
            System.out.println("Closed segment with startGap:" + startGap + " rightGap:" + rightGap +
                                " prevTotalGap:" + prevTotalGap + " prevMax:" + prevMax + " list:" +
                                toString(debugString.toCharArray())); 
            return prevMax;
        }


        /* Add the i th sequence segment.
         * 
         * Whenever this character occurence happens,
         * it checks if gaps are more than K. If yes,
         * then prevMax is calculated. If not, max
         * has not yet reached.
         */
        public void addSeq(int sIdx, int eIdx, boolean listCreated) {
            int[] sePair = new int[2];
            sePair[0] = sIdx;
            sePair[1] = eIdx;
            if (listCreated) {
                startNewSegment(sIdx, sePair);
                return;
            } else if (charList.size() == 0) {
                /* Should not happen, truncation of sequence segment
                 * never leaves the list empty, and a new sequence
                 * segment always start by adding a sequence element.
                 */
                throw new IllegalArgumentException();
            }
            
            /* second or greater sequence segment */
            int gapI = sIdx - prevEnd - 1;
            
            /* Start a new segment if current gap is more than K
             * or all the K has been used up.
             */
            if (gapI > K ) {
                closeCurrSegment(eIdx);
                startNewSegment(gapI, sePair);
            } else if (prevTotalGap + gapI > K) {
                /* Before truncating the segment: store the prevMax because the head elements might be
                 * the best max possible.
                 */
                if(debug)
                System.out.println(" prevTotalGap + gapI > K : prevTotalGap:" + prevTotalGap + " gapI:" + gapI);
                processPrevMax(startGap, gapI, prevTotalGap);
                headTruncateSegment(gapI);
                addElement(sePair, gapI);
               
            } else {
                addElement(sePair,gapI);
            }
            
        }


        private void addElement(int[] sePair, int gapI) {
            charList.add(sePair);
            prevTotalGap += gapI;
            prevEnd = sePair[1];
        }

        /* Only first prevMax can use startK, so
         * set it back to 0 after using it.
         */
        private void processPrevMax(int leftGap, int rightGap,
                                    int usedGap) {

            int currMax = charList.get(charList.size() - 1)[1]
                    - charList.get(0)[0] + 1;
            /* Note that remK will always be used up for any
             * sequence segment. In case it is not, then prevMax
             * is K and that case is taken care by the caller.*/
            int remK = K - usedGap;
            remK = Math.min((leftGap + rightGap), remK);

            currMax += remK;
            if (currMax > prevMax) {
                prevMax = currMax;
            }
            
            if(debug)
            System.out.println("Process PrevMax with usedGap:" + usedGap + " rightGap:" + rightGap +
                               " leftGap:" + leftGap + " currMax:" + currMax + "remK:" + remK +
                               " prevTotalGap:" + prevTotalGap + " prevMax:" + prevMax + " list:" +
                               toString(debugString.toCharArray()));
                               
        }
        
        /* To be called before adding ith seq pair to charList.
         * Called only when the gap gi caused crossing of K,
         * however gap gi itself is less than K. If gi > K,
         * A new sequence segment is created and whole of old
         * segment list is removed.
         * 
         * at the addition of i th sequqnce, i th gap crosses
         * K, but i th gap is less than K. This requires few older
         * sequences to get removed so that a new maximum can be tried. */
        private void headTruncateSegment(int gapI /* i th gap */) {
            if(debug)
            System.out.println("Before head truncate startGap:" + startGap +
                               " prevTotalGap:" + prevTotalGap + " prevMax:" + prevMax + " list:" +
                               toString(debugString.toCharArray())); 
            int totalPrevGap = 0;
            if (charList.size() < 2) {
                return;
            }
            int j = charList.size() - 1;
            int prevGap = 0;
            for (; j >= 0; j--) {
                int sNext = charList.get(j)[0];
                if ( j < 0) {
                    break;
                }
                int ePrev = charList.get(j - 1)[1];
                prevGap = sNext - ePrev - 1;
                totalPrevGap += prevGap;
                if ((totalPrevGap + gapI /* i th gap */) > K) {
                    if(debug)
                    System.out.println(" ... head truncate break at j:" + j + " at Gap between j and j-1" +
                                       " at prevGap:" + prevGap 
                                       + " totalPrevGap:" + totalPrevGap + " list:" +
                                       toString(debugString.toCharArray())); 
                    break;
                }
            }
            assert j >= 0;
            /* The gap between j and j+1 the seq can not be considered.
             * So Remove all nodes till (including) j */
            startGap = prevGap;
            prevTotalGap = totalPrevGap - prevGap;
            if ((j - 1)>= 0) {            
                for (int i = 0; i <= (j - 1); i++) {
                    charList.remove(0);
                    if(debug)
                    System.out.println(" ... head truncate removed index:" + i + " list:" +
                                       toString(debugString.toCharArray())); 
                }
            }
            if(debug)
            System.out.println("After head truncate startGap:" + startGap +
                               " prevTotalGap:" + prevTotalGap + " prevMax:" + prevMax + " list:" +
                               toString(debugString.toCharArray()));
                               
            
        }
        
        public String toString(char[] string) {
            StringBuilder sb = new StringBuilder();
            Character ch = null;
            if (charList.size() > 0) {
                ch = string[charList.get(0)[0]];
                sb.append("\nSequence segment for Char:" + ch + " startGap:" + startGap + " prevMax:" + prevMax + " list:\n" );
            }
            for (int[] seq: charList) {
                sb.append(" s:" + seq[0] + " e:" + seq[1] + ":gap:");
            }
            
            return sb.toString();
        }
    }
    



}
