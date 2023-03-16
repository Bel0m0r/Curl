import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.stream.*;

public class XmlCurlExample {

    public static void main(String[] args) {

        try {
            // отправляем GET запрос через curl
            String[] commands = new String[] {"curl", "-X", "GET", "http://192.168.31.80:8080/opt/out"};
            Process process = Runtime.getRuntime().exec(commands);

            InputStream inputStream = process.getInputStream();

            // создаем XMLStreamReader для чтения ответа сервера
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            // создаем объект для хранения данных
            Data data = new Data();
            data.setReplyId("12345");

            // ищем и сохраняем значение тега <ticket>
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals("ticket")) {
                    reader.next();
                    String ticket = reader.getText();
                    int startIndex = ticket.indexOf("80:8080/opt/out/");
                    if (startIndex >= 0) {
                        startIndex += "80:8080/opt/out/".length();
                        ticket = ticket.substring(startIndex);
                    }
                    data.setTicket(ticket);
                    break;
                }
            }

            inputStream.close();
            process.destroy();

            // создаем XMLStreamWriter для записи объекта в XML-файл
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(new FileOutputStream("C:\\1\\Valheim.xml"));

            // начинаем документ
            writer.writeStartDocument();
            writer.writeStartElement("data");

            // записываем значения полей объекта в XML-элементы
            writer.writeStartElement("replyId");
            writer.writeCharacters(data.getReplyId());
            writer.writeEndElement();

            writer.writeStartElement("ticket");
            writer.writeCharacters(data.getTicket());
            writer.writeEndElement();

            // закрываем элементы и документ
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();

            System.out.println("XML-файл создан: Cheque.xml");

        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    // класс для хранения данных
    public static class Data {
        private String replyId;
        private String ticket;

        public String getReplyId() {
            return replyId;
        }

        public void setReplyId(String replyId) {
            this.replyId = replyId;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }
    }
}
