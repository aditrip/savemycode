package oracle.kv.impl.tif.esclient.esRequest;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import oracle.kv.impl.tif.esclient.jsonContent.ESJsonBuilder;
import oracle.kv.impl.tif.esclient.jsonContent.JsonUtil;
import oracle.kv.impl.tif.esclient.restClient.RestRequest;

import com.fasterxml.jackson.core.JsonGenerator;

public class IndexDocumentRequest extends ESWriteRequest<IndexDocumentRequest> {

	private byte[] source;

	private boolean isRetry = false;

	/**
	 * Construct a IndexDocumentRequest. An IndexDocumentRequest should have
	 * source to index. Also, currently ESHandler uses indexing operation as an
	 * idempotent operation. So "id" field is required. However, if not given,
	 * id is generated but then it is not an idempotent operation for logEntries
	 * resulting from a restart of a stream from the checkpointed VLSN.
	 *
	 * @param index
	 *            The index to index into
	 * @param type
	 *            The type to index into
	 * @param id
	 *            The id of document
	 */
	public IndexDocumentRequest() {

	}

	public IndexDocumentRequest(String index, String type, String id) {
		super(index, type, id);
	}

	/*
	 * This is JSON source and String has UTF-8 encoding
	 */
	public IndexDocumentRequest source(String source1) {
		return source(source1.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Sets the content source to index.
	 */
	public IndexDocumentRequest source(JsonGenerator jsonGen) {
		return source(((ByteArrayOutputStream) jsonGen.getOutputTarget())
				.toByteArray());
	}

	/**
	 * Sets the document to index in bytes form.
	 */
	public IndexDocumentRequest source(byte[] source1) {
		this.source = Objects.requireNonNull(source1);
		return this;
	}

	public IndexDocumentRequest source(Map<String, Object> source1)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ESJsonBuilder jsonBuilder = new ESJsonBuilder(baos);
		jsonBuilder.value(source1);
		jsonBuilder.flushJsonGenerator();
		return source(baos.toByteArray());
	}

	/**
	 * The source of the document to index, recopied to a new array if it is
	 * unsafe.
	 */
	public byte[] source() {
		return source;
	}

	@Override
	public String toString() {
		return "{ index:" + index + " ,type:" + type + " ,id:" + id
				+ " ,source:" + JsonUtil.toStringUTF8Bytes(source) + " }";
	}

	/**
	 * Returns <code>true</code> if this request has been sent to a shard copy
	 * more than once.
	 */
	public boolean isRetry() {
		return isRetry;
	}

	public void onRetry() {
		isRetry = true;
	}

	@Override
	public IndexDocumentRequest routing(String routing1) {
		if (routing1 != null && routing1.length() == 0) {
			this.routing = null;
		} else {
			this.routing = routing1;
		}
		return this;
	}

	@Override
	public RestRequest generateRestRequest() {
		validate();
		String method = (id() != null && id().length() > 0) ? HttpPut.METHOD_NAME
				: HttpPost.METHOD_NAME;

		// String endpoint = endpoint(index(), type(), id(), "_create" );
		String endpoint = endpoint(index(), type(), id());

		RequestParams parameters = new RequestParams();
		// parameters.routing(routing());
		// parameters.version(version());
		// parameters.refreshType(refreshType());

		ContentType contentType = ContentType
				.create("application/json; charset=UTF-8");
		HttpEntity entity = new ByteArrayEntity(source, 0, source.length,
				contentType);
		System.out.println("REST REQUEST IS:"
				+ new RestRequest(method, endpoint, entity, parameters
						.getParams()));
		return new RestRequest(method, endpoint, entity, parameters.getParams());
	}

	@Override
	public RequestType requestType() {
		return RequestType.INDEX;
	}

}
