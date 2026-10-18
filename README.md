# Vem Comigo TV — APK Android

Este é um aplicativo Android que abre a interface existente em https://vem-comigo-tv.onrender.com/ e acrescenta ao modal do vídeo um botão **Abrir no player Android**. O vídeo selecionado abre no Media3 ExoPlayer. Não altera o site no Render ou o projeto Web e não inclui senhas nem playlists. É necessário conexão com internet e instalação atual do Android System WebView.

## Gerar APK sem Android Studio
1. Crie um **novo repositório** no GitHub (exemplo: `Vem-Comigo-TV-Android`). Não substitua o repositório do site.
2. Envie **todo o conteúdo desta pasta**, inclusive `.github/workflows/android-apk.yml`, à raiz do novo repositório. Não envie apenas o ZIP como um arquivo.
3. Entre na aba **Actions**, selecione **Gerar APK Vem Comigo TV**, clique **Run workflow**. Um push para `main` também inicia a compilação.
4. Após o workflow terminar com sucesso, entre na execução e, em **Artifacts**, baixe `Vem-Comigo-TV-APK`.
5. Descompacte o artefato para obter `app-debug.apk`, depois instale-o no Android.

## Observações
- **Não é um APK compilado**: este ZIP contém o código-fonte e os arquivos para compilação automática no GitHub.
- APK de **teste (debug)**, não é ainda um APK oficial assinado para distribuição na Play Store.
- O player nativo pode aceitar HTTP e mais codecs do que o navegador, mas não remove bloqueios, DRM, credenciais expiradas nem garante que cada fonte funcione.
- Para assistir, escolha um item no Vem Comigo TV, abra a janela do player e toque **▶ Abrir no player Android**.
- Os dados importados no site permanecem no armazenamento privado do WebView do APK; não são automaticamente copiados do Chrome/PWA, e por isso será necessário importar a playlist ou fazer login no Xtream dentro do APK uma vez.
- Use somente conteúdo e serviços para os quais tenha direito de acesso.
