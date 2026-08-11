import axios from "axios"
import { BucketFile, EntityWithBucketFile } from "../type/BucketFile"

export class BucketService<T extends EntityWithBucketFile> {

    protected resource: string;

    constructor(resource = "") {
        this.resource = resource;
    }

    uploadOrUpdateFile = <T extends { id?: string, bucketFile?: BucketFile }>(entity: T, file: FormData) => {
        return entity.bucketFile?.fileId
            ? axios.put(`${this.resource}/file/update/${entity.id}`, file)
            : axios.post(`${this.resource}/file/upload/${entity.id}`, file)
    }

    removeByFileId = (livroId: string, bucketFileId: string) => {
        return axios.delete(`${this.resource}/file/remove/${livroId}/${bucketFileId}`)
    }

    getFile = (fileId: string) => {
        return axios.get(`${this.resource}/file/${fileId}`,
            { responseType: "blob" },
        )
    }
}

export const bucketService = new BucketService();
