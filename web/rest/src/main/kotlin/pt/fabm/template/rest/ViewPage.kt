package pt.fabm.template.rest

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.page.PageInit

class ViewPage(private val page: PageInit) {

  fun render(): Buffer =
    Buffer.buffer().appendString(
      """
        <html>
          <head>
            <meta charset="utf-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
            <title>Car app</title>
            <link href="favicon.ico" rel="shortcut icon"/>
            <link href="main.css" rel="stylesheet"/>
          </head>
          <body>
            <div class="well">
                <div id="root">"""
    )
      .appendBuffer(page.page)
      .appendString(
        """</div>
            </div>
            <script type="text/javascript">var __state = """
      )
      .appendBuffer(page.pageInitData.toBuffer())
      .appendString(
        """.trimMargin();</script>
            <script type="text/javascript" src="bundle.js"></script>
          </body>
        </html>
        """
      )
}
