package oracle.kv.impl.tif.esclient.restClient.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;

import oracle.kv.impl.tif.esclient.esResponse.InvalidResponseException;
import oracle.kv.impl.tif.esclient.jsonContent.JsonUtil;
import oracle.kv.impl.tif.esclient.restClient.RestResponse;
import oracle.kv.impl.util.JsonUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

public class ESRestClientUtil {

	public static JsonParser initParser(RestResponse resp)
			throws InvalidResponseException {
		HttpEntity entity = resp.getEntity();
		if (entity == null) {
			throw new InvalidResponseException("Response has no httpEntity");
		} else if (!entity.getContentType().getValue().toLowerCase()
				.startsWith("application/json")) {
			throw new InvalidResponseException(
					"ContentType is not application/json");
		} else {
			try {
				return JsonUtil.createParser(entity.getContent());
			} catch (Exception pe) {
				throw new InvalidResponseException(
						"ParseException: Response JSON could not be parsed");
			}
		}
	}

	/* Call this method only after checking if mapping for the given
	 * esIndexName and esIndexType exists.
	 * 
	 * There are no null checks while getting the relevant mapping spec
	 * from mapping response.
	 * 
	 */
	public static boolean isMappingResponseEqual(String mappingResponse,
			JsonGenerator mappingSpec, String esIndexName, String esIndexType)
			throws IOException {
		try {
			mappingSpec.flush();
			String mappicSpecStr = new String(
					((ByteArrayOutputStream) mappingSpec.getOutputTarget())
							.toByteArray(),
					"UTF-8");
			Map<String, Object> mappingSpecMap = JsonUtils
					.getMapFromJsonStr(mappicSpecStr);
			Map<String, Object> existingMappingMap = JsonUtils
					.getMapFromJsonStr(mappingResponse);

			/*
			 * Note that this method is called
			 * after checking that mapping exists.
			 * hence no null checks done here.
			 */
			@SuppressWarnings("unchecked")
			Map<String, Object> existingMappingExtract = (Map<String, Object>) ((Map<String, Object>) (((Map<String, Object>) (existingMappingMap
					.get(esIndexName))).get("mappings"))).get(esIndexType);

			System.out.println("ExistingMappingExtract:" + existingMappingExtract);
			System.out.println("CreatedMappingSpec    :" + mappingSpecMap);
			return mappingSpecMap.equals(existingMappingExtract);
			
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
