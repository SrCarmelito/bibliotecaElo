import { useEffect, useState } from "react";
import { Button, Image, Modal, Upload } from "antd";
import { DeleteTwoTone, EditTwoTone, ExclamationCircleFilled, InboxOutlined } from "@ant-design/icons";
import { useNotification } from "../contexts/notificationContext";
import { UploadRequestOption } from "rc-upload/lib/interface";
import Title from "antd/lib/typography/Title";
import ImgCrop from "antd-img-crop";
import { EntityWithBucketFile } from "../type/BucketFile";
import { BucketService } from "../service/BucketService";

type BucketFileProps<T extends EntityWithBucketFile> = {
  entity: T;
  width: number;
  title?: string;
  fetchData?: () => void;
  canEdit?: boolean;
  canRemove?: boolean;
  preview?: boolean;
  bucketService: BucketService<T>;
};

export function BucketFileRender<T extends EntityWithBucketFile>({ entity, width, title, fetchData, canEdit, canRemove, preview, bucketService }: BucketFileProps<T>) {
  const [src, setSrc] = useState("");
  const [modal, contextHolder] = Modal.useModal();
  const openNotification = useNotification();

  useEffect(() => {
    if (entity.bucketFile?.id) {
      bucketService.getFile(entity.bucketFile?.id).then((response) => {
        const imageUrl = URL.createObjectURL(response.data);
        setSrc(imageUrl);
      });

      return () => {
        if (src) {
          URL.revokeObjectURL(src);
        }
      };
    }
  }, [entity]);

  const uploadOrUpdate = async ({ file }: UploadRequestOption) => {
    const formData = new FormData();
    formData.append("multipartFile", file);

    bucketService.uploadOrUpdateFile(entity, formData)
      .then(() => {
        openNotification("success", "imagem inserida com sucesso");
        fetchData?.();
      })
  };

  function onRemoveFile(): void {
    if (entity.id && entity.bucketFile?.id) {
      modal.confirm({
        title: "Deseja remover a imagem?",
        icon: <ExclamationCircleFilled />,
        onOk() {
          if (entity.id && entity.bucketFile?.id) {
            bucketService.removeByFileId(entity.id, entity.bucketFile?.id)
              .then(() => {
                fetchData?.();
                openNotification("success", "Imagem removida com sucesso.");
              })
          }
        },
      });
    }
  }

  return (
    <div>
      <Title level={5}>{title ? title : "Imagem"}</Title>
      {entity.bucketFile?.id &&
        <div >
          <Image src={src} width={width} preview={preview} />
          <div style={{ textAlign: "center" }}>
            {canRemove &&
              <Button
                icon={<DeleteTwoTone />}
                type="link"
                onClick={onRemoveFile}
              />
            }
            {canEdit &&
              <ImgCrop
                modalTitle="Pré visualização da imagem"
              >
                <Upload
                  customRequest={uploadOrUpdate}
                  showUploadList={false}
                >
                  <Button
                    icon={<EditTwoTone />}
                    type="link"
                  />
                </Upload>
              </ImgCrop>
            }
          </div>
        </div>
      }
      {
        entity.id && !entity.bucketFile?.id &&
        <ImgCrop
          modalTitle="Pré visualização da imagem"
        >
          <Upload
            customRequest={uploadOrUpdate}
            type="drag"
            showUploadList={false}
            accept="image/*"
            beforeUpload={(file) => {
              if (!file.type.startsWith("image/")) {
                openNotification("error", "Insira apenas arquivos de imagem");
                return false;
              }

              return true;
            }}
          >
            <p className="ant-upload-drag-icon">
              <InboxOutlined />
            </p>
            <p className="ant-upload-text">
              Clique aqui ou arraste imagens para anexar
            </p>
          </Upload>
        </ImgCrop>
      }
      {contextHolder}
    </div >
  );
};